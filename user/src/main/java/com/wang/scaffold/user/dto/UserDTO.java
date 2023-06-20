package com.wang.scaffold.user.dto;


import com.wang.scaffold.user.dto.vo.RoleVO;
import com.wang.scaffold.user.dto.vo.UserVO;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.entity.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class UserDTO {

	private String username;

	private String name;

	private String avatar;

	private String phone;

	private String email;

	private boolean disabled;

	private List<RoleVO> roles;

	private String _id;

	public UserDTO(User user) {
		this._id = new UserVO(user).get_id();
		this.username = user.getUsername();
		this.name = user.getName();
		this.avatar = user.getAvatar();
		this.phone = user.getPhone();
		this.email = user.getEmail();
		this.disabled = user.isDisabled();
		Stream<Role> stream = user.getRoles().stream();
		// 不获取role下的permission
		this.roles = stream.map(r -> {
			RoleVO role = new RoleVO(new Role());
			role.setId(r.getId());
			role.setName(r.getName());
			return role;
		}).collect(Collectors.toList());
	}
}
