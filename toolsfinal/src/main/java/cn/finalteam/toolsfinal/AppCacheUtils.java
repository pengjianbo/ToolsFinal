package cn.finalteam.toolsfinal;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.HashMap;
import java.util.Map;

import cn.finalteam.toolsfinal.coder.MD5Coder;
import cn.finalteam.toolsfinal.io.FileUtils;

/**
 * Desction:数据/配置存储类
 * Author:pengjianbo
 * Date:15/9/17 下午4:41
 */
public class AppCacheUtils {


    public static final String DEFAULT_CACHE_NAME = "appCache";

    private File mCacheFile;
    private static Map<String, AppCacheUtils> mCacheUtilsMap = new HashMap<>();

    private AppCacheUtils(File cacheFile) {
        mCacheFile =  cacheFile;
        FileUtils.mkdirs(cacheFile);
    }

    public static AppCacheUtils getInstance(Context ctx) {
        File storeFile = StorageUtils.getCacheDirectory(ctx, false, DEFAULT_CACHE_NAME);
        return getInstance(storeFile);
    }

    public static AppCacheUtils getInstance(File file) {
        AppCacheUtils appCacheUtils = mCacheUtilsMap.get(file.getAbsolutePath());
        if (appCacheUtils == null) {
            appCacheUtils = new AppCacheUtils(file);
            mCacheUtilsMap.put(file.getAbsolutePath(), appCacheUtils);
        }
        return appCacheUtils;
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

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {

        if (cn.finalteam.toolsfinal.StringUtils.isEmpty(key)) {
            return;
        }

        if ( cn.finalteam.toolsfinal.StringUtils.isEmpty(value) ) {
            value = "";
        }
        File file = newFile(key);
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
        }
    }
    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        if(value == null || value.length == 0 || cn.finalteam.toolsfinal.StringUtils.isEmpty(key)){
            return;
        }
        File file = newFile(key);
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
        }
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        if(value == null){
            return;
        }
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        if(value == null){
            return;
        }
        put(key, value.toString());
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value) {
        if(cn.finalteam.toolsfinal.StringUtils.isEmpty(key) || value == null) {
            return;
        }
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            put(key, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    public int getInt(String key, int defValue) {
        String sValue = getString(key);
        if (!cn.finalteam.toolsfinal.StringUtils.isEmpty(sValue)) {
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
        if (!cn.finalteam.toolsfinal.StringUtils.isEmpty(sValue)) {
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
        if (!cn.finalteam.toolsfinal.StringUtils.isEmpty(sValue)) {
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
        if (!cn.finalteam.toolsfinal.StringUtils.isEmpty(sValue)) {
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
        if (!cn.finalteam.toolsfinal.StringUtils.isEmpty(sValue)) {
            try {
                boolean bValue = Boolean.parseBoolean(sValue);
                return bValue;
            } catch (Exception e) {
            }
        }

        return defValue;
    }

    /**
     * 读取 String数据
     *
     * @return String 数据
     */
    public String getString(String key) {
        if(cn.finalteam.toolsfinal.StringUtils.isEmpty(key)) {
            return null;
        }
        File file = newFile(key);
        if (!file.exists()) {
            return null;
        }
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

    /**
     * 读取 Serializable数据
     *
     * @return Serializable 数据
     */
    public Object getObject(String key) {
        if ( cn.finalteam.toolsfinal.StringUtils.isEmpty(key) ) {
            return null;
        }
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

    /**
     * 获取 byte 数据
     *
     * @return byte 数据
     */
    public byte[] getBinary(String key) {
        if ( cn.finalteam.toolsfinal.StringUtils.isEmpty(key) ) {
            return null;
        }
        RandomAccessFile rAFile = null;
        byte[] byteArray = null;
        try {
            File file = newFile(key);
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

    private File newFile(String key) {
        return new File(mCacheFile, MD5Coder.getMD5Code(key));
    }

    /**
     * 移除缓存
     * @param key
     */
    public void remove(String key) {
        try {
            newFile(key).delete();
        } catch (Exception e){}
    }
}
