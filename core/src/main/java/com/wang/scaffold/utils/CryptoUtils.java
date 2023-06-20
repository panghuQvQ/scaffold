package com.wang.scaffold.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * 密码、密钥、签名等等等等等等等等等等等等
 * @author zhou wei
 *
 */
public final class CryptoUtils {

	private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 生成32位，即256bit的key字符串（可手动输入性比较OK的）
	 * @return
	 */
	public static String generate32bytesKey() {
		SecureRandom sr = new SecureRandom();
		char[] chars = DIGITS.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 32; i++) {
			int index = sr.nextInt(DIGITS.length());
			sb.append(chars[index]);
		}
		return sb.toString();
	}

    /**
     * 生成 HMACSHA256 签名
     * @param data 待处理数据
     * @param key 密钥
     * @return 签名字符串(hex)
     * @throws Exception
     */
    public static String signWithHMACSHA256(String data, String key) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);
        byte[] bytes = sha256HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * 自然排序,拼接模式key1=value1key2=value2key3=value3...
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String signWithHMACSHA256(Map<String, Object> data, String key) throws Exception {
    	TreeMap<String, Object> map = new TreeMap<String, Object>(data);
    	StringBuilder sb = new StringBuilder();
    	map.forEach((k, v) -> {
    		sb.append(k).append("=").append(v);
    	});
        return signWithHMACSHA256(sb.toString(), key);
    }

}
