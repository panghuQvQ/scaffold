package com.wang.scaffold.sharded.security.token;

import com.wang.scaffold.sharded.security.JwtProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * jwt token生成器，通过{@link UserDetails}生成。<br>
 * <br>
 * 使用方法: <br>
 * JwtTokenGenerator.withProperties(properties) 静态方法返回对象。
 * @see JwtProperties
 */
public class JwtTokenGenerator {

	private JwtProperties properties;

	private JwtTokenGenerator() {}

	public static JwtTokenGenerator withProperties(JwtProperties properties) {
		Objects.requireNonNull(properties);
		JwtTokenGenerator jtg = new JwtTokenGenerator();
		jtg.properties = properties;
		return jtg;
	}

	public String generateToken(String username, Map<String, List<?>> claims) {
		return generateToken(username, claims, null);
	}

	public String generateToken(String username, Map<String, List<?>> claims, String id) {
		byte[] signingKey = properties.getSecret().getBytes();
		JwtBuilder builder = Jwts.builder();
		builder.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256) // 第一个参数为：秘钥，使用 HS256对称加密算法签名
		.setHeaderParam("typ", properties.getTokenType())
		.setHeaderParam("v", properties.getVersion())
		.setIssuer(properties.getTokenIssuer()) // 签发者
		.setAudience(properties.getTokenAudience())
		.setSubject(username) // 主题  可以是JSON数据
		.setExpiration(new Date(System.currentTimeMillis() + properties.getExpire())); // 设置过期时间
		if (id != null && id.trim().length() > 0) {
			builder.setId(id.trim()); // 唯一的ID
		}
		if (claims != null) {
			claims.forEach((k, v) -> {
				builder.claim(k, v);
			});
		}
		return builder.compact();
	}


}
