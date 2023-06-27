package com.wang.scaffold.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/** 用户 */
@ToString(exclude = "password")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_user")
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, unique = true, nullable = false)
	private String username;

	@Column(length = 50)
	private String name;
	/** 头像 **/
	private String avatar;
	/** 手机号码 */
	@Column(unique = true)
	private String phone;
	/** 邮箱 */
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY )
	@Column(nullable = false)
	private String password;
	/** 账号禁用 */
	private boolean disabled;

	@JsonProperty(access = Access.WRITE_ONLY )
	@ManyToMany
	@JoinTable(name = "app_user_role", joinColumns = @JoinColumn(name= "user_id"), inverseJoinColumns = @JoinColumn(name= "role_id"))
	private List<Role> roles;

	public void replaceRoles(List<Role> roles) {
		this.roles.clear();
		if (roles != null) {
			this.roles.addAll(roles);
		}
	}

	public User() {}

	public User(Integer id, String username, String name) {
		this.id = id;
		this.username = username;
		this.name = name;
	}
}
