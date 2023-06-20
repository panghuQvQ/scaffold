package com.wang.scaffold.user.auth;

/**
 * 遭到攻击异常
 * @author zhou wei
 *
 */
public class UnderAttactException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnderAttactException(String message) {
		super(message);
	}
}
