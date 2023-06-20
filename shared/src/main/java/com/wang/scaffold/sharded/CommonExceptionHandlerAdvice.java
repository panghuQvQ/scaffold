package com.wang.scaffold.sharded;

import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.exception.VersionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CommonExceptionHandlerAdvice {

    @ExceptionHandler(VersionException.class)
    public BaseResponse<?> handleVersionException(VersionException ex, HttpServletRequest request) {
        return BaseResponse.fail(400, ex.getMessage());
    }
}
