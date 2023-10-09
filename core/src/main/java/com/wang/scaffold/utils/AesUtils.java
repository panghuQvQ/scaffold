package com.wang.scaffold.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * 算法:AES/CBC/PKCS5Padding
 * <br/>256bit key
 * <br/>使用密码加密解密
 * <br/>加密、解密字符串用Base64编码
 *
 * @author zhou wei
 */
public final class AesUtils {
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Base64
     * ll9Er+pQ2A7oV/G30PEfKg==
     */
    private static final byte[] IV_BYTES = {-106, 95, 68, -81, -22, 80, -40, 14, -24, 87, -15, -73, -48, -15, 31, 42};
    /**
     * Base64
     * CieU4C3KVsANALqexXl4pQsHs6C1O899Khm3UrqJvZ0=
     */
    private static final byte[] SALT_BYTES = {10, 39, -108, -32, 45, -54, 86, -64, 13, 0, -70, -98, -59, 121, 120, -91, 11, 7, -77, -96, -75, 59, -49, 125, 42, 25, -73, 82, -70, -119, -67, -99};

    private static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT_BYTES, 1024, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static IvParameterSpec generateIv() {
        return new IvParameterSpec(IV_BYTES);
    }

    public static String encrypt(String input, String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(password), generateIv());
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(password), generateIv());
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

//    public static void main(String[] args) {
//        System.out.println(Base64.getEncoder().encodeToString(IV_BYTES));
//        System.out.println(Base64.getEncoder().encodeToString(SALT_BYTES));
//    }
}
