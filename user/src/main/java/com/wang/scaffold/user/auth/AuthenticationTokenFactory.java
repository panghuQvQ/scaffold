package com.wang.scaffold.user.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

import com.wang.scaffold.user.auth.details.CaptchaAuthenticationDetails;
import com.wang.scaffold.user.auth.details.DefaultAuthenticationDetails;
import com.wang.scaffold.user.auth.phone.PhoneCodeAuthenticationToken;
import com.wang.scaffold.utils.AesUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationTokenFactory {

	private static final String PRIVATE_KEY_BASE64_STR = "-----BEGIN PRIVATE KEY-----"
			+ "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDiVOHrAyNqIyl/"
			+ "MBvPi5oZu4Ue2VSHaeDyKBdyQUUdb+Jz/WhQyg5gykk3GgIvMzAxi2iOtZ74WdqN"
			+ "7wT9MadGVgqi7/gxNJyCFpy1qoEE5dDFGX7hGXSTs3X2R4DCvhLqJgJ5Mwkhq8nh"
			+ "vCOTQlBGvx24IHgmrA4s6OAbe7anDBODH55xEz6yQjWAH4Mf4My+t5dpTI2Zy1jH"
			+ "zwneyE++hsWavdwtatwzzd5sdT8jxnPZGcgtLZy9XFBs1qY+jfhMhVrLV/x8cZOv"
			+ "uklpioajD0xZo+DF3JLe/B93JzYWIoWgxt6d4sAv/pcmpJIGmH9bLIQxfyDsEwGh"
			+ "/0lEttzLAgMBAAECggEBAJgOqxF3NE3UfcajjausgMWVBGSrYmzvp19JrCbozwRt"
			+ "7Ng8gd71QR+qB/JrGNiZLWG75W0rfDgiNgFxegqZV2ab292IF1kHnbaQdoxelvux"
			+ "Ysdoz6NvtLCeuzvQ4iL0DEUfcD35GcNzZNixy7zdqo09TTwtJEvnAEcP1bDhNt+W"
			+ "q77vKsTv6Hlwpx+o4ts/Xl82t3u/KKXbhXcWMl2sBK1PRsWKyTlSNVSo58tRvZYF"
			+ "TSFXjY5/wPjJJeAF8gi7J+Lv3ueGZBWLEG1j6vuZ1XVnHmHxwzchpJd2DitvI8bL"
			+ "MZQB/FObnoak3begj6vzSAM7dwNYN5pG0mmX4PtdHKECgYEA+jgN1rfJk4J+H7lo"
			+ "5NpHriioGMrw0sWw5QSK0kOjpTyn76OA7Q+2EwvqXYjFA60fP6rWTgE/686jyZ8T"
			+ "8ybV/th6Jgjx8uUVmoWBB+OUGhEz6UXAK//PCrnmYW+/iJRMzWC2igQaTuk4/UDY"
			+ "pfvIJbD8WG+7M9OHGLdaP27DSUMCgYEA54+LQ5/IXSngGzdtfuLNN5KB7Q0XmLRt"
			+ "Lkp738P93x5F7tuEtkCbPvVcnGmgnyTRlTFpMdSvMP5KY8ucPneWku+8ojip8yYU"
			+ "e1qwYB7ZOAxt0EM0aZE+PrRyubTEBhTTAhV1sY+P2JruLqtB5YcAAPjDuQOxAE2d"
			+ "wWaXKZABgdkCgYEAwLKAfdamR5KcXQQl8vDjeQ1Yf1dy5W9XzteTrWBn2639ywCJ"
			+ "HogV+U8Y25Wj3DPsN9TyG7q8pnZKDtESxRQog43la/tBkYS5AXpFJ3UtLnIHH2QX"
			+ "HgtwQFtwP6OWxrEnAJe1nE6SkCjuOaNB9XoU5moJvHW3nq8NgIxb1TQQ8w8CgYA0"
			+ "DWR00xorNxqVg6QtfbY748cpDmRbg1N3sG6gImKTIS7/PoFaprM/N9CVqHjezVv/"
			+ "eZf4B6NKFB+Mx1xb/m3uoc15edtaORkpkX1MXiLeIo0c376MbIXdepq/fA8EfvHf"
			+ "6gsG2wqu2kToYL7zZaFuln0Ivfev5cKsku8v/JsaIQKBgQDZcVje2pUgBrrF83+J"
			+ "+PhycgX4RbIcVOYtqd8MORsiKNF8P26/RVx1/B2gYTuwgjozKRd3M+WwRWeyCbYr"
			+ "hmwoS/jPgOv8cAL6EAJMMPnVyfU8o1VY2VPG2rmmCWomuQdybL3gm/X4ldMscxvw"
			+ "Z2Tyw1zMEbLi5EHlWJ3xsyvjRg=="
			+ "-----END PRIVATE KEY-----";
//	private static final String PUBLIC_KEY_BASE64_STR = "-----BEGIN PUBLIC KEY-----"
//			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4lTh6wMjaiMpfzAbz4ua"
//			+ "GbuFHtlUh2ng8igXckFFHW/ic/1oUMoOYMpJNxoCLzMwMYtojrWe+Fnaje8E/TGn"
//			+ "RlYKou/4MTScghactaqBBOXQxRl+4Rl0k7N19keAwr4S6iYCeTMJIavJ4bwjk0JQ"
//			+ "Rr8duCB4JqwOLOjgG3u2pwwTgx+ecRM+skI1gB+DH+DMvreXaUyNmctYx88J3shP"
//			+ "vobFmr3cLWrcM83ebHU/I8Zz2RnILS2cvVxQbNamPo34TIVay1f8fHGTr7pJaYqG"
//			+ "ow9MWaPgxdyS3vwfdyc2FiKFoMbeneLAL/6XJqSSBph/WyyEMX8g7BMBof9JRLbc"
//			+ "ywIDAQAB"
//			+ "-----END PUBLIC KEY-----";


	public static Authentication buildAuthToken(HttpServletRequest request) {
		String loginType = request.getHeader("x-login-type");
		if ("RSA_U_PWD".equals(loginType)) {
			try {
				String encryptedStr = request.getReader().readLine();
				UsernamePasswordAuthenticationToken token = decryptUsernamePassword(encryptedStr);
				token.setDetails(new DefaultAuthenticationDetails(request));
				return token;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		String phone = request.getParameter("phone");
		String code = request.getParameter("code");
		// 手机号码和短信验证码
		if (StringUtils.hasText(phone) && StringUtils.hasText(code)) {
			PhoneCodeAuthenticationToken phoneAuthenticationToken = new PhoneCodeAuthenticationToken(phone, code);
			phoneAuthenticationToken.setDetails(new DefaultAuthenticationDetails(request));
			return phoneAuthenticationToken;
		}

		String username = request.getParameter("8DpMnT");
		String password = request.getParameter("0yOXPM");
		// 默认的web登录
		// 用户名密码
		if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
			UsernamePasswordAuthenticationToken unamePwdAuthToken = new UsernamePasswordAuthenticationToken(username, password);
			unamePwdAuthToken.setDetails(new CaptchaAuthenticationDetails(request));
			return unamePwdAuthToken;
		}

		// AES加密登录，timestamp是password
		String timestamp = request.getParameter("timestamp");
		if (StringUtils.hasText(timestamp)) {
			try {
				String encryptedStr = request.getReader().readLine();
				String loginJson = AesUtils.decrypt(encryptedStr, timestamp);
				ObjectMapper mapper = new ObjectMapper();
				username = mapper.readTree(loginJson).get("username").asText();
				password = mapper.readTree(loginJson).get("password").asText();
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
				token.setDetails(new DefaultAuthenticationDetails(request));
				return token;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private static UsernamePasswordAuthenticationToken decryptUsernamePassword(String encryptedStr) {
		UsernamePasswordAuthenticationToken result = null;

		byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedStr);

		String privateKeyPEM = PRIVATE_KEY_BASE64_STR
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replaceAll(System.lineSeparator(), "")
				.replace("-----END PRIVATE KEY-----", "");
		byte[] prvKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
		try {
			EncodedKeySpec prvlicKeySpec = new PKCS8EncodedKeySpec(prvKeyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey prvKey = keyFactory.generatePrivate(prvlicKeySpec);
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.DECRYPT_MODE, prvKey);
			byte[] decryptedMessageBytes = encryptCipher.doFinal(encryptedMessageBytes);

			String jsonStr = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

			ObjectMapper mapper = new ObjectMapper();
			String username = mapper.readTree(jsonStr).get("username").asText();
			String password = mapper.readTree(jsonStr).get("password").asText();
			long timestamp = mapper.readTree(jsonStr).path("timestamp").asLong();
			result = new UsernamePasswordAuthenticationToken(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//	public static void main(String[] args) throws Exception {
	//		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
	//		generator.initialize(2048);
	//		KeyPair pair = generator.generateKeyPair();
	//		PrivateKey privateKey = pair.getPrivate();
	//		PublicKey publicKey = pair.getPublic();
	//		OutputStreamWriter pukFos = new OutputStreamWriter(new FileOutputStream("/Users/weizhou/devfiles/public.key"));
	//		OutputStreamWriter prkFos = new OutputStreamWriter(new FileOutputStream("/Users/weizhou/devfiles/private.key"));
	//		Encoder encoder = Base64.getEncoder();
	//		pukFos.write(encoder.encodeToString(publicKey.getEncoded()));
	//		prkFos.write(encoder.encodeToString(privateKey.getEncoded()));
	//		pukFos.close();
	//		prkFos.close();
	//	}

	//	public static void main(String[] args) throws Exception {
	//		String publicKeyPEM = PUBLIC_KEY_BASE64_STR
	//				.replace("-----BEGIN PUBLIC KEY-----", "")
	//				.replaceAll(System.lineSeparator(), "")
	//				.replace("-----END PUBLIC KEY-----", "");
	//		byte[] pubKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
	//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	//		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubKeyBytes);
	//		PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
	//
	//		Cipher encryptCipher = Cipher.getInstance("RSA");
	//		encryptCipher.init(Cipher.ENCRYPT_MODE, pubKey);
	//
	//		String secretMessage = "Baeldung secret message";
	//		byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
	//		byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
	//
	//		String privateKeyPEM = PRIVATE_KEY_BASE64_STR
	//				.replace("-----BEGIN PRIVATE KEY-----", "")
	//				.replaceAll(System.lineSeparator(), "")
	//				.replace("-----END PRIVATE KEY-----", "");
	//		byte[] prvKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
	//		EncodedKeySpec prvlicKeySpec = new PKCS8EncodedKeySpec(prvKeyBytes);
	//		PrivateKey prvKey = keyFactory.generatePrivate(prvlicKeySpec);
	//		encryptCipher.init(Cipher.DECRYPT_MODE, prvKey);
	//		byte[] decryptedMessageBytes = encryptCipher.doFinal(encryptedMessageBytes);
	//		System.out.println(new String(decryptedMessageBytes, StandardCharsets.UTF_8));
	//	}
}
