package cn.paojiao.toolsfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Desction:数据/配置存储类
 * Author:pengjianbo
 * Date:15/9/17 下午4:41
 */
public class AppCacheUtils {

    private static final long MAX_SIZE = Long.MAX_VALUE;
    private static final long MAX_COUNT = Long.MAX_VALUE; // 不限制存放数据的数量
    private static Map<String, AppCacheUtils> mInstanceMap = new HashMap<>();
    private ACacheManager mCache;

    public static AppCacheUtils get(Context ctx) {
        return get(ctx, "appCache");
    }

    public static AppCacheUtils get(Context ctx, String cacheName) {
        File f = new File(StorageUtils.getCacheDirectory(ctx), cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static AppCacheUtils get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    public static AppCacheUtils get(Context ctx, long max_zise, long max_count) {
        File f = new File(StorageUtils.getCacheDirectory(ctx), "appCache");
        return get(f, max_zise, max_count);
    }

    public static AppCacheUtils get(File cacheDir, long max_zise, long max_count) {
        AppCacheUtils manager = mInstanceMap.get(cacheDir.getAbsoluteFile());
        if (manager == null) {
            manager = new AppCacheUtils(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath(), manager);
        }
        return manager;
    }

    private AppCacheUtils(File cacheDir, long max_size, long max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + cacheDir.getAbsolutePath());
        }
        mCache = new ACacheManager(cacheDir, max_size, max_count);
    }

    public void put(String key, int value) {
        put(key, value + "");
    }

    public void put(String key, float value) {
        put(key, value + "");
    }

    public void put(String key, double value) {
        put(key, value + "");
    }

    public void put(String key, boolean value) {
        put(key, value + "");
    }

    public void put(String key, long value) {
        put(key, value + "");
    }

    public int getInt(String key, int defValue) {
        String sValue = getString(key);
        if (!StringUtils.isEmpty(sValue)) {
            try {
                int iValue = Integer.parseInt(sValue);
                return iValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    public float getFloat(String key, float defValue) {
        String sValue = getString(key);
        if (!StringUtils.isEmpty(sValue)) {
            try {
                float fValue = Float.parseFloat(sValue);
                return fValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    public Double getDouble(String key, double defValue) {
        String sValue = getString(key);
        if (!StringUtils.isEmpty(sValue)) {
            try {
                double dValue = Double.parseDouble(sValue);
                return dValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    public long getLong(String key, long defValue) {
        String sValue = getString(key);
        if (!StringUtils.isEmpty(sValue)) {
            try {
                long dValue = Long.parseLong(sValue);
                return dValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        String sValue = getString(key);
        if (!StringUtils.isEmpty(sValue)) {
            try {
                boolean bValue = Boolean.parseBoolean(sValue);
                return bValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        if ( StringUtils.isEmpty(value) ) {
            value = "";
        }
        File file = mCache.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * 读取 String数据
     *
     * @return String 数据
     */
    public String getString(String key) {
        File file = mCache.get(key);
        if (!file.exists()) { return null; }
        BufferedReader in = null;
        String readString = "";
        try {
            in = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            return readString;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readString;
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONObject数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONObject数据
     *
     * @return JSONObject数据
     */
    public JSONObject getJSONObject(String key) {
        String JSONString = getString(key);
        try {
            JSONObject obj = new JSONObject(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONArray数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONArray数据
     *
     * @return JSONArray数据
     */
    public JSONArray getJSONArray(String key) {
        String JSONString = getString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        File file = mCache.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * 获取 byte 数据
     *
     * @return byte 数据
     */
    public byte[] getBinary(String key) {
        RandomAccessFile rAFile = null;
        byte[] byteArray = null;
        try {
            File file = mCache.get(key);
            if (!file.exists()) { return null; }
            rAFile = new RandomAccessFile(file, "r");
            long fLength = rAFile.length();
            if ( fLength != 0 ) {
                byteArray = new byte[(int) rAFile.length()];
                rAFile.read(byteArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rAFile != null) {
                try {
                    rAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArray;
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================

    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的value
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取 Serializable数据
     *
     * @return Serializable 数据
     */
    public Object getObject(String key) {
        byte[] data = getBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject = ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null) { bais.close(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null) { ois.close(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        put(key, Utils.Bitmap2Bytes(value));
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的 bitmap 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Bitmap value, int saveTime) {
        put(key, Utils.Bitmap2Bytes(value), saveTime);
    }

    /**
     * 读取 bitmap 数据
     *
     * @return bitmap 数据
     */
    public Bitmap getBitmap(String key) {
        if (getBinary(key) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(getBinary(key));
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        put(key, Utils.drawable2Bitmap(value));
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的 drawable 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Drawable value, int saveTime) {
        put(key, Utils.drawable2Bitmap(value), saveTime);
    }

    /**
     * 读取 Drawable 数据
     *
     * @return Drawable 数据
     */
    public Drawable getDrawable(String key) {
        if (getBinary(key) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getBinary(key)));
    }

    /**
     * 获取缓存文件
     *
     * @return value 缓存的文件
     */
    public File file(String key) {
        File f = mCache.newFile(key);
        if (f.exists()) { return f; }
        return null;
    }

    /**
     * 移除某个key
     *
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     * @title 缓存管理器
     */
    public class ACacheManager {
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final long countLimit;
        private final Map<File, Long> lastUsageDates = Collections
                .synchronizedMap(new HashMap<File, Long>());
        protected File cacheDir;

        private ACacheManager(File cacheDir, long sizeLimit, long countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        /**
         * 计算 cacheSize和cacheCount
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size += calculateSize(cachedFile);
                            count += 1;
                            lastUsageDates.put(cachedFile,
                                    cachedFile.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file) {
            int curCacheCount = cacheCount.get();
            while (curCacheCount + 1 > countLimit) {
                long freedSize = removeNext();
                cacheSize.addAndGet(-freedSize);

                curCacheCount = cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);

            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            while (curCacheSize + valueSize > sizeLimit) {
                long freedSize = removeNext();
                curCacheSize = cacheSize.addAndGet(-freedSize);
            }
            cacheSize.addAndGet(valueSize);

            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);

            return file;
        }

        private File newFile(String key) {
            return new File(cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File image = get(key);
            return image.delete();
        }

        private void clear() {
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /**
         * 移除旧的文件
         */
        private long removeNext() {
            if (lastUsageDates.isEmpty()) {
                return 0;
            }

            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Map.Entry<File, Long>> entries = lastUsageDates.entrySet();
            synchronized (lastUsageDates) {
                for (Map.Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    } else {
                        Long lastValueUsage = entry.getValue();
                        if (lastValueUsage < oldestUsage) {
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }

            long fileSize = calculateSize(mostLongUsedFile);
            if (mostLongUsedFile.delete()) {
                lastUsageDates.remove(mostLongUsedFile);
            }
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    /**
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     * @title 时间计算工具类
     */
    private static class Utils {
        /*
         * Bitmap → byte[]
         */
        private static byte[] Bitmap2Bytes(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }

        /*
         * byte[] → Bitmap
         */
        private static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        /*
         * Drawable → Bitmap
         */
        private static Bitmap drawable2Bitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        }

        /*
         * Bitmap → Drawable
         */
        @SuppressWarnings("deprecation")
        private static Drawable bitmap2Drawable(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            return new BitmapDrawable(bm);
        }
    }
}
