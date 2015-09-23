package cn.finalteam.toolsfinal;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Desction:Des加密
 * Author:pengjianbo
 * Date:15/9/22 下午7:42
 */
public class DesCoder {
    /**
     * 单位长
     */
    private static final int UNIT_LENGTH = 8;
    /**
     * 加密算法
     */
    public final static String KEY_ALGORITHM = "DES";
    /**
     * 加密算法/工作模式/填充方式
     */
    public final static String CHIPER_ALGORITHM = "DES/CBC/NoPadding";
    /**
     * 初始化变量
     */
    public final static byte[] IvParameter = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte[] subByte(byte b[], int start, int end) {
        int sublength = end - start;
        byte[] result = new byte[sublength];
        System.arraycopy(b, start, result, 0, sublength);
        return result;
    }

    /**
     * 8个字节做异或
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] doXor(byte[] b1, byte[] b2) {
        int byte_length = 8;
        byte[] result = new byte[byte_length];

        if (b1.length != byte_length || b2.length != byte_length) {
            throw new IllegalArgumentException("Both byte array'length must = 8!");
        }

        for (int i = 0; i < b1.length; i++) {
            result[i] = (byte) (b1[i] ^ b2[i]);
        }

        return result;
    }
    /**
     * 加密
     * @param msg
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] msg,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);

            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(dks);
            IvParameterSpec spec = new IvParameterSpec(IvParameter);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey,spec);
            return cipher.doFinal(msg);
        } catch (Exception e) {
            throw new RuntimeException("Error when encrypt key, ermsg: " + e.getMessage(), e);
        }

    }

    /**
     * 解密
     * @param msg
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] msg,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);

            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(dks);
            IvParameterSpec spec = new IvParameterSpec(IvParameter);

            cipher.init(Cipher.DECRYPT_MODE, secretKey,spec);
            return  cipher.doFinal(msg);
        } catch (Exception e) {

            throw new RuntimeException("Error when decrypt key, ermsg: " + e.getMessage(), e);
        }

    }

    /**
     * 补位操作
     * @param srcBytes
     * @return
     */
    public static byte[] fillGap(byte[] srcBytes){
        try {
            int len = srcBytes.length;
            int total = (len/8 + 1) * 8;

            byte[] destBytes = new byte[total];
            System.arraycopy(srcBytes, 0, destBytes, 0, len);
            if(total-len==1){
                destBytes[len] = (byte) 0x80;
            }else{
                for(int i=len ; i<total ; i++){
                    if(i==len){
                        destBytes[i] = (byte)0x80;
                    }else{
                        destBytes[i] = (byte)0x00;
                    }

                }
            }
            return destBytes;
        } catch (Exception e) {
            throw new RuntimeException(" Error when msg fill gap "+ e.getMessage(), e);
        }
    }

    /**
     * 初始数据
     * @param srcBytes
     * @return
     */
    public static byte[] initData(byte[] srcBytes){
        byte[] initBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        int len = srcBytes.length;
        byte[] destBytes = new byte[len+8];
        System.arraycopy(initBytes, 0, destBytes, 0, 8);
        System.arraycopy(srcBytes, 0, destBytes, 8, len);
        return destBytes;
    }

    /**
     * 初始数据和补位操作
     * @param srcBytes
     * @return
     */
    public static byte[] initAndFillGap(byte[] srcBytes){
        return fillGap(initData(srcBytes));
    }
}
