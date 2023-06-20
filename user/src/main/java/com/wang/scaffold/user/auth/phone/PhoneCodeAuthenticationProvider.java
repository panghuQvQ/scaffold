package com.wang.scaffold.user.auth.phone;

import com.wang.scaffold.sharded.exception.PhoneNotFoundException;
import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class PhoneCodeAuthenticationProvider implements AuthenticationProvider {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Autowired private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(PhoneCodeAuthenticationToken.class, authentication, "Only PhoneCodeAuthenticationToken is supported");
		PhoneCodeAuthenticationToken authToken = (PhoneCodeAuthenticationToken) authentication;
		String phone = authToken.getPrincipal().toString();
		String code = authToken.getCode();
		log.debug("手机号:{}, 验证码: {}", phone, code);
		// 校验手机验证码
		this.checkCode(phone, code);
		LoginUser loginUser = userService.loadUserByPhone(phone);
		// 校验账号
		this.checkAccount(loginUser);

		PhoneCodeAuthenticationToken result = new PhoneCodeAuthenticationToken(loginUser, code, loginUser.getAuthorities());
		result.setDetails(authToken.getDetails());

		return result;
	}

	// TODO 短信验证码校验
	private void checkCode(String phone, String code) {
		throw new InternalAuthenticationServiceException("短信验证码登录接口暂未实现。");
	}

	// copy from AbstractUserDetailsAuthenticationProvider#preAuthenticationChecks
	private void checkAccount(UserDetails user) {
		if(user == null) {
			throw new PhoneNotFoundException("未找到手机号码绑定的用户");
		}
		if (!user.isAccountNonLocked()) {
			log.debug("User account is locked");
			throw new LockedException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.locked",
					"User account is locked"));
		}

		if (!user.isEnabled()) {
			log.debug("User account is disabled");

			throw new DisabledException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.disabled",
					"User is disabled"));
		}

		if (!user.isAccountNonExpired()) {
			log.debug("User account is expired");

			throw new AccountExpiredException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.expired",
					"User account has expired"));
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PhoneCodeAuthenticationToken.class);
	}

}
