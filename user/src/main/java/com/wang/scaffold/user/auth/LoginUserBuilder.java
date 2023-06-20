package com.wang.scaffold.user.auth;

import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title 登录用户，构造器
 * @description 当用户信息认证成功后，通过改构造器，生成 LoginUser 对象
 * @author wzy
 * @updateTime 2023/6/20 14:37
 * @throws
 */
public class LoginUserBuilder {

	private LoginUser loginUser;

	private LoginUserBuilder(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	public static LoginUserBuilder from(User user) {
		LoginUser lu = new LoginUser();
		lu.setUsername(user.getUsername());
		lu.setName(user.getName());
		lu.setPassword(user.getPassword());
		lu.setEnabled(!user.isDisabled());
		List<Integer> userRoles = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
		lu.setRoleIds(userRoles);
//		HashSet<String> permSet = new HashSet<String>();
//		user.getRoles().forEach(r -> {
//			r.getPermissions().forEach(p -> {
//				permSet.add(p.getName());
//			});
//		});
//		lu.setPerms(permSet);

		LoginUserBuilder builder = new LoginUserBuilder(lu);
		return builder;
	}

	public LoginUserBuilder device(String device) {
		loginUser.setDevice(device);
		return this;
	}

	public LoginUserBuilder token(String token) {
		loginUser.setToken(token);
		return this;
	}

	public LoginUserBuilder refreshToken(String refreshToken) {
		loginUser.setRefreshToken(refreshToken);;
		return this;
	}

	public LoginUserBuilder erasePassword() {
		loginUser.setPassword("");
		return this;
	}

	public LoginUser build() {
		return loginUser;
	}
}
