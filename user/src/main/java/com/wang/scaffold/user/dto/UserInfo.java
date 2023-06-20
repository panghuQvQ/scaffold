package com.wang.scaffold.user.dto;

import lombok.Data;

import java.util.Set;

/**
 * 用户信息
 */
@Data
public class UserInfo {

	private String username;

	private String name;
	/** 头像 */
	private String avatar;
	/** 联系电话 */
	private String phone;
	/** 邮箱 */
	private String email;

	private String department;

	private Set<String> perms;

}
