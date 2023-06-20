package com.wang.scaffold.user.auth.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 验证账号密码或者手机号码之前的校验拦截。如果失败，请抛出异常。
 *
 * @author weizhou
 * @see CaptchaInterceptor
 */
public interface PreAuthenInterceptor {

	/**
	 * 在Authentication token创建成功后，在正式查询数据库校验前，可能需要验证码，验证图形点击或者请求次数限制之类的拦截。
	 * @param authenToken
	 * @throws AuthenticationException
	 */
	void preAuthenticate(Authentication authenToken) throws AuthenticationException;

}
