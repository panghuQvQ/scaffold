package com.wang.scaffold.sharded.security.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wang.scaffold.consts.WebConstants;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.user.LoginUser;

@JsonInclude(value = Include.NON_NULL)
public class TokenResponse extends BaseResponse<LoginUser> {

	private static final long serialVersionUID = 1L;

	public static TokenResponse success(LoginUser user) {
		TokenResponse response = new TokenResponse();
		response.setSuccess(true);
		response.setCode(WebConstants.SECCUSS_OK_CODE);
		response.setMessage("OK");
		response.setItem(user);
		return response;
	}

	public static TokenResponse wrongPassword() {
		TokenResponse response = new TokenResponse();
		response.setCode(WebConstants.WRONG_PASSWORD);
		response.setSuccess(false);
		response.setMessage("用户名或密码错误");
		return response;
	}

	public static TokenResponse accountDisabled() {
		TokenResponse response = new TokenResponse();
		response.setCode(WebConstants.ACCOUNT_DISABLED);
		response.setSuccess(false);
		response.setMessage("账号已被停用，请联系管理员。");
		return response;
	}

	public static TokenResponse expire() {
		TokenResponse response = new TokenResponse();
		response.setCode(WebConstants.TOKEN_EXPIRE);
		response.setSuccess(true);
		response.setMessage("Token expired!");
		return response;
	}

	public static TokenResponse loginExpire() {
		TokenResponse response = new TokenResponse();
		response.setCode(WebConstants.LOGIN_EXPIRE);
		response.setSuccess(true);
		response.setMessage("登录过期，请重新登录。");
		return response;
	}
}
