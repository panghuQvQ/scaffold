package com.wang.scaffold.user.auth;

public class BadTokenException extends RuntimeException {

	private static final long serialVersionUID = 8717049515574492485L;

	public BadTokenException(String message) {
		super(message);
	}

}
