package com.wang.scaffold.sharded.exception;

import org.springframework.security.core.AuthenticationException;

public class PhoneNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public PhoneNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	public PhoneNotFoundException(String msg) {
		super(msg);
	}

}
