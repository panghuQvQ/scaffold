package com.wang.scaffold.user.entity;

import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户偏好
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_user_preference")
public class UserPreference extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	/** 用户名 */
	private String username;
	/** 键 */
	private String preferenceKey;
	/** 值 */
	private String preferenceValue;
}
