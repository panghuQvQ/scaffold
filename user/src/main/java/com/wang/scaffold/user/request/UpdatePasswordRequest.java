package com.wang.scaffold.user.request;

import lombok.Data;

/**
 * 修改密码请求参数
 *
 * @author gu ping
 *
 */
@Data
public class UpdatePasswordRequest {
	// 旧密码
	private String oldPassword;
	// 新密码
	private String newPassword;

}
