package com.wang.scaffold.user;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


public final class PasswordTest {

	private static Set<String> commonPasswords = new HashSet<>();

	static {
		InputStream ins = PasswordTest.class.getResourceAsStream("/common-passwords.txt");
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(ins));
		String temp;
		try {
			while ((temp = buffReader.readLine()) != null) {
				commonPasswords.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buffReader.close();
				ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static final int PASSWORD_MIN_LENGTH = 8;
	public static final int PASSWORD_MAX_LENGTH = 20;

	public static final void test(String password, String username) throws Exception {
		int length = password.length();
		if (!StringUtils.hasText(password)) {
			throw new NullPointerException("密码不能为空");
		}
		if (password.equals(username)) {
			throw new NullPointerException("账号和密码不能相同");
		}
		if (length < PASSWORD_MIN_LENGTH || length > PASSWORD_MAX_LENGTH) {
			throw new IllegalArgumentException("密码长度需在" + PASSWORD_MIN_LENGTH + "-" + PASSWORD_MAX_LENGTH + "之间");
		}
		if (password.matches("[0-9]{"+ length +"}") || password.matches("[a-zA-Z]{"+ length +"}")) {
			throw new IllegalArgumentException("密码不能为纯数字或纯字母");
		}
		if (commonPasswords.contains(password)) {
			throw new IllegalArgumentException(password + "为弱口令,请更换其他密码");
		}

	}
}
