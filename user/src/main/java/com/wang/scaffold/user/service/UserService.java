package com.wang.scaffold.user.service;


import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.dto.UserInfo;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.entity.User;
import com.wang.scaffold.user.request.UpdateUserStatusRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	LoginUser loadUserByPhone(String phone);

	List<User> getAllUsers();

	void configRoles(Integer userId, List<Role> roles);

	User save(User user);

	/**
	 * 管理员删除用户
	 *
	 * @param userId 用户id
	 */
	void delete(Integer userId, String deleteReason);

	/**
	 * 修改密码
	 *
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 */
	void updatePassword(String oldPassword, String newPassword);

	/**
	 * 修改用户状态
	 *
	 * @param request
	 */
	void updateStatus(UpdateUserStatusRequest request);

	/**
	 * 查询用户信息
	 * @param username
	 * @return
	 */
	UserInfo findUserInfo(String username);

	/**
	 * 更新用户详细信息
	 * @param userInfo
	 * @return
	 */
	void updateUserDetail(UserInfo userInfo);

	List<User> findUserByDisabled(boolean disabled);

	List<String> getUsernameByRoleName(String roleName);

	void checkDefaultUsers();
}
