package com.wang.scaffold.user.auth;

import com.wang.scaffold.consts.WebConstants;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.sharded.security.token.JwtTokenParser;
import com.wang.scaffold.sharded.security.token.TokenResponse;
import com.wang.scaffold.sharded.user.LoginUser;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/session")
public class SessionController {

	@Autowired
	JwtProperties jwtProperties;
	@Autowired AuthTokenService authTokenService;

	@GetMapping("/validate")
	public BaseResponse<?> validate(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("Authorization");
		if (!StringUtils.isEmpty(token) && token.startsWith("Bearer ")) {
			token = token.replaceFirst("Bearer ","");
			try {
				JwtTokenParser.useSecret(jwtProperties.getSecret()).testToken(token, jwtProperties.getVersion());
				return BaseResponse.success();
			} catch (ExpiredJwtException e) {
				//token已经过期，告知客户端要刷新
				return TokenResponse.expire();
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return TokenResponse.fail("登录已经失效，请重新登录。");
	}

	/**
	 * 登出
	 * @return
	 */
	@PostMapping("/terminate")
	public BaseResponse<?> terminate(String rtoken) {
		authTokenService.deleteToken(rtoken);
		return BaseResponse.success();
	}

	/**
	 * 刷新token
	 * <p>step1、通过refreshToken从数据库查询token</p>
	 * <p>step2、比对token</p>
	 * <p>step3、生成新的token，生成新的refreshToken</p>
	 * @param rtoken
	 * @return
	 */
	@PostMapping("/refresh-token")
	public BaseResponse<?> refreshToken(String rtoken) {
		try {
			LoginUser updated = authTokenService.refreshToken(rtoken);
			return TokenResponse.success(updated);
		} catch (AccountLockedException e) {
			return BaseResponse.fail(WebConstants.ACCOUNT_LOCKED, "账号已被锁定");
		} catch (UnderAttactException e) {
			return BaseResponse.fail(WebConstants.UNDER_ATTACT, "登录已失效，请重新登录。");
		} catch (Exception e) {
			if (e instanceof BadTokenException && e.getMessage().contains("过期")) {
				return TokenResponse.loginExpire();
			}
			e.printStackTrace();
			return BaseResponse.fail(e.getMessage());
		}
	}

}
