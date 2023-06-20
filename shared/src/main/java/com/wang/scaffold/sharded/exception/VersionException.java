package com.wang.scaffold.sharded.exception;

import org.springframework.security.core.AuthenticationException;

public class VersionException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public VersionException(String msg, Throwable t) {
        super(msg, t);
    }

    public VersionException(String msg) {
        super(msg);
    }

}
