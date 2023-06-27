package com.wang.scaffold.user.entity;

import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户活跃记录
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_user_active_log", indexes = @Index(columnList = "username, activeDate", unique = true))
public class UserActiveLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name="uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "uuid")
	private String _id;

	@Column(updatable = false, length = 50)
	private String username;

	@Column(updatable = false, length = 16)
	private String activeDate;

	private int times;
}
