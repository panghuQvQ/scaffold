package com.wang.scaffold.user.request;

import lombok.Data;

import java.util.List;

/**
 * 	新增修改用户请求参数
 * @author gu ping
 *
 */
@Data
public class UserRequest {
	private Integer id;
	private String username;
	private String name;
	private List<String> rolesName;
}
