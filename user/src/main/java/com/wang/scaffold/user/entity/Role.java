package com.wang.scaffold.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** 角色 */
@Data
@EqualsAndHashCode(callSuper=false, exclude = "permissions")
@Entity
@Table(name = "app_role")
public class Role extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id @JsonIgnore
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Permission> permissions;

	public void addPermission(Permission permission) {
		if (this.permissions == null) this.permissions = new ArrayList<>();
		if (permission != null) {
			permission.setRole(this);
			this.permissions.add(permission);
		}
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
		if (this.permissions != null) {
			for (Permission permission : permissions) {
				permission.setRole(this);
			}
		}
	}

	public void replacePermissions(List<Permission> permissions) {
		this.permissions.clear();
		if (permissions != null) {
			this.permissions.addAll(permissions);
		}
	}
}
