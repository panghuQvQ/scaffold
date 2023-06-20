package com.wang.scaffold.sharded.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt配置，在application配置文件中配置
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	/**
	 *  验证地址
	 */
	private String authUrl;

	/**
	 *  HS256 key
	 */
	private String secret;

	/**
	 *  token过期时间，单位：毫秒
	 */
	private long expire = 600 * 1000;

	/**
	 *  刷新token过期时间，单位：毫秒
	 */
	private long refreshTokenExpire = 1 * 8 * 3600 * 1000;

	/**
	 *  移动端刷新token过期时间，单位：毫秒
	 */
	private long refreshTokenExpireMobile = 15 * 24 * 3600 * 1000;

	private String tokenType = "JWT";

	private String tokenIssuer = "api";

	private String tokenAudience = "app";

	private String version = "0.0.1";

	/**
	 * 验证地址
	 * @return
	 */
	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	/**
	 * HS256 key
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * token过期时间，单位：毫秒
	 * @return
	 */
	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	/**
	 * refresh token过期时间，单位：毫秒
	 * @return
	 */
	public long getRefreshTokenExpire() {
		return refreshTokenExpire;
	}

	public void setRefreshTokenExpire(long refreshTokenExpire) {
		this.refreshTokenExpire = refreshTokenExpire;
	}

	/**
	 * 移动端refresh token过期时间，单位：毫秒
	 * @return
	 */
	public long getRefreshTokenExpireMobile() {
		return refreshTokenExpireMobile;
	}

	public void setRefreshTokenExpireMobile(long refreshTokenExpireMobile) {
		this.refreshTokenExpireMobile = refreshTokenExpireMobile;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public String getTokenAudience() {
		return tokenAudience;
	}

	public void setTokenAudience(String tokenAudience) {
		this.tokenAudience = tokenAudience;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
