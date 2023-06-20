package com.wang.scaffold.sharded.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常
 * @author weizhou
 *
 */
public class CaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	private int code;

	public CaptchaException(String msg, Throwable t) {
		super(msg, t);
	}

	public CaptchaException(String msg) {
		super(msg);
	}

	public CaptchaException(String msg, int code) {
		super(msg);
		this.code = code;
	}

	public CaptchaException(String msg, Throwable t, int code) {
		super(msg, t);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
