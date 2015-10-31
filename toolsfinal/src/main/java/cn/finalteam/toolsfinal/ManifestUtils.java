package cn.finalteam.toolsfinal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Desction:Android Manifest工具类
 * Author:pengjianbo
 * Date:15/9/17 下午4:38
 */
public class ManifestUtils {

    /**
     * 获取Manifest Meta Data
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaData(Context context, String metaKey) {
        String name = context.getPackageName();
        ApplicationInfo appInfo;
        String msg = "";
        try {
            appInfo = context.getPackageManager().getApplicationInfo(name,
                    PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(msg)) {
            return "";
        }

        return msg;
    }

    /**
     * 获得渠道号
     * @param context
     * @param channelKey
     * @return
     */
    public static String getChannelNo(Context context, String channelKey) {
        return getMetaData(context, channelKey);
    }

    /**
     * 获得apk版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(version)) {
            version = "";
        }

        return version;
    }

    /**
     * 获得apk版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }
}
