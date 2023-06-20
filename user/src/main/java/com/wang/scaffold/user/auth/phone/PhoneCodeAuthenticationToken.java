package com.wang.scaffold.user.auth.phone;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final Object principal;
	private Object credentials;
	private String code;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>PhoneCodeAuthenticationToken</code>, as the
	 * {@link #isAuthenticated()} will return <code>false</code>.
	 *
	 */
	public PhoneCodeAuthenticationToken(String phone, String code) {
		super(null);
		this.principal = phone;
		this.code = code;
		setAuthenticated(false);
	}

	/**
	 * This constructor should only be used by <code>AuthenticationManager</code> or
	 * <code>AuthenticationProvider</code> implementations that are satisfied with
	 * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
	 * authentication token.
	 *
	 * @param principal
	 * @param credentials
	 * @param authorities
	 */
	public PhoneCodeAuthenticationToken(Object principal, String code,
                                        Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.code = code;
		super.setAuthenticated(true); // must use super, as we override
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public String getCode() {
		return this.code;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		code = null;
	}
}
