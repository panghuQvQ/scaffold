package com.wang.scaffold.user.service;


import com.wang.scaffold.user.entity.Role;

import java.util.List;

public interface RoleService {

	List<Role> getAllRoles();

	Role save(Role role);

	void delete(Integer roleId);

	// 检查默认角色
	void checkDefaultRoles();
}
