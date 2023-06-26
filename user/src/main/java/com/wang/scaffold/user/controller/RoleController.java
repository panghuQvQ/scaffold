package com.wang.scaffold.user.controller;


import com.wang.scaffold.annotation.SysLog;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.response.CollectionResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.user.SystemUserRoleTest;
import com.wang.scaffold.user.dto.vo.RoleVO;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/role")
@RestController
public class RoleController {

	@Autowired
	RoleService roleService;

	@GetMapping("/list")
	public CollectionResponse<?> getAllRoles() {
		List<Role> items = roleService.getAllRoles();
		String username = WebAppContextHelper.currentUsername();
		if (!SystemUserRoleTest.isSystemUser(username)) {
			items = items.stream().filter(r -> {
				return !SystemUserRoleTest.isSystemRole(r);
			}).collect(Collectors.toList());
		}
		return CollectionResponse.success(items.stream().map(RoleVO::new).collect(Collectors.toList()));
	}

	@PreAuthorize("hasAuthority('role')")
	@SysLog(module = "角色管理", operation = "新增/更新")
	@PostMapping("/save")
	public BaseResponse<?> save(@RequestBody RoleVO role) {
		Role r = roleService.save(role.entity());
		return BaseResponse.success(new RoleVO(r));
	}

	@PreAuthorize("hasAuthority('role')")
	@SysLog(module = "角色管理", operation = "删除")
	@PostMapping("/delete")
	public BaseResponse<?> delete(String roleId) {
		RoleVO temp = new RoleVO();
		temp.set_id(roleId);
		try {
			roleService.delete(temp.getId());
			return BaseResponse.success();
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage());
		}
	}
}
