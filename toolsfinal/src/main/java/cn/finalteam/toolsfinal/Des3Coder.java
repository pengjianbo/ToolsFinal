package cn.finalteam.toolsfinal;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Desction:三层des编码
 * Author:pengjianbo
 * Date:15/9/22 下午7:41
 */
public class Des3Coder {

    public final static String KEY_ALGORITHM = "DESede";

    public final static String CHIPER_ALGORITHM = "DESede/ECB/NoPadding";

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] msg, byte[] key) {
        byte[] des3Key = getKey(key);

        try {
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(des3Key, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(fillGap(msg));
        } catch (Exception e) {
            throw new RuntimeException("Error when encrypt key, ermsg: " + e.getMessage(), e);
        }
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] msg, byte[] key) {
        byte[] des3Key = getKey(key);
        try {
            Cipher cipher = Cipher.getInstance(CHIPER_ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(des3Key, KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return removeGap(cipher.doFinal(msg));
        } catch (Exception e) {

            throw new RuntimeException("Error when drcrypt key, ermsg: " + e.getMessage(), e);
        }
    }

    /**
     * 得到3Deskey
     */
    private static byte[] getKey(byte[] key) {
        if (key.length != 16) {
            throw new IllegalArgumentException(" key length is not 16 bytes.");
        }
        byte[] des3Key = new byte[24];
        System.arraycopy(key, 0, des3Key, 0, 16);
        System.arraycopy(key, 0, des3Key, 16, 8);
        return des3Key;
    }

    /**
     * 补位操作
     */
    public static byte[] fillGap(byte[] srcBytes) {
        try {
            int len = srcBytes.length;
            int total = (len / 8 + 1) * 8;

            byte[] destBytes = new byte[total];
            System.arraycopy(srcBytes, 0, destBytes, 0, len);
            if (total - len == 1) {
                destBytes[len] = (byte) 0x80;
            } else {
                for (int i = len; i < total; i++) {
                    if (i == len) {
                        destBytes[i] = (byte) 0x80;
                    } else {
                        destBytes[i] = (byte) 0x00;
                    }
                }
            }
            return destBytes;
        } catch (Exception e) {
            throw new RuntimeException(" Error when msg fill gap " + e.getMessage(), e);
        }
    }

    /**
     * 移位操作
     */
    public static byte[] removeGap(byte[] srcBytes) {
        byte[] desBytes;
        try {
            int len = srcBytes.length;
            int pos = 0;
            for (int i = len - 1; i >= len - 9; i--) {
                if (srcBytes[i] == -128) {
                    pos = i;
                    break;
                }
            }
            if (pos == 0) {
                desBytes = srcBytes;
            } else {
                desBytes = new byte[pos];
                System.arraycopy(srcBytes, 0, desBytes, 0, pos);
            }
            return desBytes;
        } catch (Exception e) {
            throw new RuntimeException(" Error when msg remove gap " + e.getMessage(), e);
        }
    }
}
