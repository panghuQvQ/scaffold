package com.wang.scaffold.sharded.security.token;

import com.wang.scaffold.utils.ComparableVersion;
import io.jsonwebtoken.*;

import java.util.Objects;

/**
 * jwt Token解析，返回 {@link ParsedJwtToken}对象<br>
 * 使用方法: <br>
 * JwtTokenParser.useSecret(secret) 静态方法返回对象。
 */
public class JwtTokenParser {

	private byte[] signingKey;

	private JwtTokenParser() {}

	public static JwtTokenParser useSecret(String secret) {
		Objects.requireNonNull(secret);
		JwtTokenParser jtp = new JwtTokenParser();
		jtp.signingKey = secret.getBytes();
		return jtp;
	}

	/**
	 * 测试token是否能可用
	 * @param token
	 * @param ver 需要与客户端对比的服务端token version，不需要对比可以传入null
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws IllegalArgumentException
	 */
	public void testToken(String token, String ver) {
		Jws<Claims> parsedToken = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token);
		if (ver != null) {
			String version = parsedToken.getHeader().get("v").toString();
			ComparableVersion clientVersion = new ComparableVersion(version);
			ComparableVersion serverVersion = new ComparableVersion(ver);
			if (serverVersion.compareTo(clientVersion) > 0) {
				throw new RuntimeException("Token version is outdated.");
			}
		}
	}

	/**
	 * @see io.jsonwebtoken.JwtParser#parseClaimsJws
	 *
	 * @param token
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws IllegalArgumentException
	 */
	public ParsedJwtToken parseToken(String token) {
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.setAllowedClockSkewSeconds(5)
				.build()
				.parseClaimsJws(token);
		ParsedJwtToken result = new ParsedJwtToken(jws);
		return result;
	}
}
