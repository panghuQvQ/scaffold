package com.wang.scaffold.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.wang.scaffold.consts.WebConstants.*;


/**
 *
 * @author zhou wei
 *
 */
@ToString
@JsonInclude(Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success;

	private int code;

	private String message;

	private T item;

	private Map<String, Object> others = new HashMap<>();

	public BaseResponse() {
	}

	protected BaseResponse(boolean success, int code) {
		this(success, code, null, null);
	}

	protected BaseResponse(boolean success, int code, String message) {
		this(success, code, message, null);
	}

	protected BaseResponse(boolean success, int code, String message, T item) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.item = item;
	}

	public static <T> BaseResponse<T> success() {
		return new BaseResponse<T>(true, SECCUSS_OK_CODE, SECCUSS_OK_MSG);
	}

	public static <T> BaseResponse<T> success(String message) {
		return new BaseResponse<T>(true, SECCUSS_OK_CODE, message);
	}

	public static <T> BaseResponse<T> success(T any) {
		return new BaseResponse<T>(true, SECCUSS_OK_CODE, SECCUSS_OK_MSG, any);
	}

	public static <T> BaseResponse<T> success(String message, T any) {
		return new BaseResponse<T>(true, SECCUSS_OK_CODE, message, any);
	}

	public static <T> BaseResponse<T> fail() {
		return new BaseResponse<T>(false, FAIL_INTERNAL_CODE, FAIL_INTERNAL_MSG);
	}

	public static <T> BaseResponse<T> fail(String message) {
		return new BaseResponse<T>(false, FAIL_INTERNAL_CODE, message);
	}

	public static <T> BaseResponse<T> fail(int code, String message) {
		return new BaseResponse<T>(false, code, message);
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	@JsonAnyGetter
	public Map<String, Object> getOthers() {
		return others;
	}

	public BaseResponse<?> put(String key, Object value) {
		this.others.put(key, value);
		return this;
	}
}
