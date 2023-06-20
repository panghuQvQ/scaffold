package com.wang.scaffold.exception;


import com.wang.scaffold.consts.WebConstants;

/**
 * @author twgu
 * @date 2020/10/16
 * @desc
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer code;

    /**
     *
     */
    public BaseException() {
        super();
    }

    /**
     * @param message
     */
    public BaseException(String message) {
        super(message);
        this.code = WebConstants.FAIL_INTERNAL_CODE;
    }

    /**
     * @param code
     * @param message
     */
    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
