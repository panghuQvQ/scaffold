package com.wang.scaffold.user.auth;

import com.wang.scaffold.sharded.security.token.Constants;
import com.wang.scaffold.sharded.security.token.JwtTokenGenerator;
import com.wang.scaffold.sharded.user.LoginUser;
import io.jsonwebtoken.JwtBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部使用
 */
abstract class TokenUtils {

	/**
	 * claims中包含role id集合
	 * <pre>claim("groups", List&lt;Integer&gt; roleIds)</pre>
	 * @see {@link JwtBuilder#claim(String, Object)}
	 */
	public static final String genertateToken(JwtTokenGenerator generator, LoginUser user) {
		Map<String, List<?>> claims = new HashMap<>();
		claims.put(Constants.GROUPS, user.getRoleIds());
		String token = generator.generateToken(user.getUsername(), claims);
		return token;
	}
}
