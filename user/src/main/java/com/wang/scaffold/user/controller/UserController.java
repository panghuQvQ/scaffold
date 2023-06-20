package com.wang.scaffold.user.controller;


import com.wang.scaffold.annotation.SysLog;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.response.CollectionResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.security.token.TokenResponse;
import com.wang.scaffold.user.PasswordTest;
import com.wang.scaffold.user.SystemUserRoleTest;
import com.wang.scaffold.user.dto.UserDTO;
import com.wang.scaffold.user.dto.UserInfo;
import com.wang.scaffold.user.dto.vo.RoleVO;
import com.wang.scaffold.user.dto.vo.UserVO;
import com.wang.scaffold.user.entity.User;
import com.wang.scaffold.user.request.UpdatePasswordRequest;
import com.wang.scaffold.user.request.UpdateUserStatusRequest;
import com.wang.scaffold.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
public class UserController {

	@Autowired private UserService userService;
	@Autowired private PasswordEncoder passwordEncoder;

	@PreAuthorize("hasAuthority('user')") // 在方法调用之前,基于表达式的计算结果来限制对方法的访问
	@GetMapping("/list")
	public CollectionResponse<?> getAllUsers() {
		List<User> items = userService.getAllUsers();
		String username = WebAppContextHelper.currentUsername();
		if (!SystemUserRoleTest.isSystemUser(username)) {
			items = items.stream().filter(u -> !SystemUserRoleTest.isSystemUser(u)).collect(Collectors.toList());
		}

		List<UserDTO> result = items.stream().map(e -> {
			UserDTO dto = new UserDTO(e);
			return dto;
		}).collect(Collectors.toList());
		return CollectionResponse.success(result);
	}

	@SysLog(module = "用户管理", operation = "更新-配置角色")
	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/roles/{userId}")
	public BaseResponse<?> configRoles(@PathVariable String userId, @RequestBody List<RoleVO> roles) {
		UserVO temp = new UserVO();
		temp.set_id(userId);
		userService.configRoles(temp.getId(), roles.stream().map(RoleVO::entity).collect(Collectors.toList()));
		return BaseResponse.success();
	}

	@SysLog(module = "用户管理", operation = "新增/更新", serializationType = "toString")
	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/save")
	public BaseResponse<?> save(@RequestBody UserVO userVO) {
		User user = userVO.entity();
		if (!StringUtils.isEmpty(user.getPassword())) {
			try {
				PasswordTest.test(user.getPassword(), user.getUsername());
			} catch (Exception e) {
				return BaseResponse.fail(e.getMessage());
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		try {
			User u = userService.save(user);
			return BaseResponse.success("OK", new UserVO(u));
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage());
		}

	}

	@SysLog(module = "用户管理", operation = "删除")
	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/delete")
	public BaseResponse<?> delete(String userId, String deleteReason) {
		UserVO temp = new UserVO();
		temp.set_id(userId);
		try {
			userService.delete(temp.getId(), deleteReason);
			return BaseResponse.success();
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage());
		}

	}

	@GetMapping("/info")
	public BaseResponse<?> userInfo() {
		String username = WebAppContextHelper.currentUsername();
		try {
			UserInfo userInfo = userService.findUserInfo(username);
			return BaseResponse.success("OK", userInfo);
		} catch (DisabledException e) {
			return TokenResponse.expire(); // 账号已禁用，使其token失效退出登录，无法获取自己的用户信息。
		}
	}

	@SysLog(module = "用户管理", operation = "更新-账户状态")
	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/update-status")
	public BaseResponse<?> updateUserStatus(@RequestBody UpdateUserStatusRequest request) {
		try {
			userService.updateStatus(request);
			return BaseResponse.success("修改成功");
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage());
		}
	}

	@SysLog(module = "用户管理", operation = "更新-更新密码", ignoreParameters = "request") // 请勿记录密码到日志！
	@PostMapping("/update-pwd")
	public BaseResponse<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
		try {
			PasswordTest.test(request.getNewPassword(), WebAppContextHelper.currentUsername());
			userService.updatePassword(request.getOldPassword(), request.getNewPassword());
			return BaseResponse.success("修改成功");
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage() + ",密码修改失败！");
		}
	}

	/**
	 * 更新当前用户详细信息
	 * @param userInfo
	 * @return
	 */
	@SysLog(module = "用户管理", operation = "更新-个人信息")
	@PostMapping("/update-info")
	public BaseResponse<?> updateUserInfo(@RequestBody UserInfo userInfo) {
		try {
			userService.updateUserDetail(userInfo);
			return BaseResponse.success("修改成功");
		} catch (Exception e) {
			return BaseResponse.fail(e.getMessage());
		}
	}

	@RequestMapping("/getUsernameByRoleName")
	public BaseResponse<List<String>> getUsernameByRoleName(@RequestParam String roleName){
		try {
			return BaseResponse.success(userService.getUsernameByRoleName(roleName));
		}catch (Exception e){
			return BaseResponse.fail(e.getMessage());
		}
	}
}
