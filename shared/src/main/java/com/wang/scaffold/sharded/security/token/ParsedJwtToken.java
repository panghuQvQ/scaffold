package com.wang.scaffold.sharded.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 解析后的jwt token对象，包含token中的信息，eg: username, Authorities
 */
public class ParsedJwtToken {

	private Jws<Claims> jws;

	public ParsedJwtToken(Jws<Claims> jws) {
		Objects.requireNonNull(jws);
		this.jws = jws;
	}

	public String getUsername() {
		return jws.getBody().getSubject();
	}

	public String getId() {
		return jws.getBody().getId();
	}

	@SuppressWarnings("unchecked")
	public <R> R getClaim(String key) {
		return (R)jws.getBody().get(key);
	}

	public <R> R getClaim(String key, Function<Object, R> fun) {
		Object value = jws.getBody().get(key);
		if (value == null) {
			return null;
		}
		return fun.apply(value);
	}

	public <R> List<R> getClaimList(String key, Function<Object, R> fun) {
		Object value = jws.getBody().get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			List<?> v = (List<?>) value;
			return v.stream().map(fun).collect(Collectors.toList());
		}
		return null;
	}

	public String getVersion() {
		return jws.getBody().get("v").toString();
	}

	@SuppressWarnings("unchecked")
	public <R> R getHeader(String key) {
		return (R) jws.getHeader().get(key);
	}

	public <R> R getHeader(String key, Function<Object, R> fun) {
		Object value = jws.getBody().get(key);
		if (value == null) {
			return null;
		}
		return fun.apply(value);
	}
}
