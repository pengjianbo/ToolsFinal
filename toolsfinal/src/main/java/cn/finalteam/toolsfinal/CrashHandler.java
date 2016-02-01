package cn.finalteam.toolsfinal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Desction:crash收集
 * Author:pengjianbo
 * Date:15/10/31 上午9:08
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
    private final static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
    private Context mContext;// 程序的Context对象
    private boolean mCrashSave;//是否保存crash信息
    private String mCrashSaveTargetFolder;
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> mDeviceInfoMap = new HashMap<>();
    /**
     * Crash事件回调
     */
    private OnCrashListener mOnCrashListener;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {}

    /**
     * 获取CrashHandler实例 ,单例模式
     * @return
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     * @param context
     * @return
     */
    public CrashHandler init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
        return this;
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(final Throwable ex) {
        Thread.setDefaultUncaughtExceptionHandler(null);
        if (ex == null) {
            return false;
        }

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        final String crashMsg = writer.toString();

        new Thread() {
            public void run() {
                Looper.prepare();
                if(mOnCrashListener != null) {
                    mOnCrashListener.onCrash(mContext, crashMsg);
                }
                Looper.loop();
            }
        }.start();

        if ( mCrashSave ) {
            // 收集设备参数信息
            collectDeviceInfo(mContext);
            // 保存日志文件
            saveCrashInfo2File(crashMsg);
        }
        return true;
    }

    /**
     * 设置Crash事件回调
     * @param listener
     * @return
     */
    public CrashHandler setOnCrashListener(OnCrashListener listener) {
        this.mOnCrashListener = listener;
        return this;
    }

    /**
     * 设置是否保存crash
     * @param isSave
     * @return
     */
    public CrashHandler setCrashSave(boolean isSave) {
        this.mCrashSave  = isSave;
        return this;
    }

    /**
     * 自定义crash保存目标文件夹
     * @param targetFolder
     * @return
     */
    public CrashHandler setCrashSaveTargetFolder(String targetFolder) {
        this.mCrashSaveTargetFolder = targetFolder;
        return this;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                mDeviceInfoMap.put("versionName", versionName);
                mDeviceInfoMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceInfoMap.put(field.getName(), field.get("").toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存crash信息
     * @param crashMsg
     * @return
     */
    private String saveCrashInfo2File(String crashMsg) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mDeviceInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }

        sb.append(crashMsg);
        // 保存文件
        long timetamp = System.currentTimeMillis();
        SimpleDateFormat dateFomat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分
        String time = dateFomat.format(new Date());
        String fileName = "crash-" + time + "-" + timetamp + ".log";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir;
                if( StringUtils.isEmpty(mCrashSaveTargetFolder) ) {
                    dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash");
                } else {
                    dir = new File(mCrashSaveTargetFolder);
                }

                if (!dir.exists()) {
                    dir.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public interface OnCrashListener{
        void onCrash(Context context, String errorMsg);
    }
}
