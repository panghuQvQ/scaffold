package com.wang.scaffold.sharded.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @title 登录用户：使用用户名密码登录进系统的用户。
 * @description 实现 UserDetails
 * @author wzy
 * @updateTime 2023/6/20 14:39
 * @throws
 */
@ToString(exclude = "password")
@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class LoginUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;
	private String name;
	private String token;
	private String refreshToken;
	private String device;
	/** 角色Id集合 */
	@JsonIgnore
	private List<Integer> roleIds = new ArrayList<>();
	@JsonIgnore
	private Set<String> perms = new HashSet<String>();
	@JsonIgnore
	private String password;
	@JsonIgnore
	private boolean enabled;
	@JsonIgnore
	private List<SimpleGrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			authorities = perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}
		return authorities;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 对象是否启用
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
