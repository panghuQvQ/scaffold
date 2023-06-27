package com.wang.scaffold.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@JsonIgnoreProperties({"createdBy", "createdTime", "updatedBy", "updatedTime"})
@ToString(exclude = "role")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_permission")
public class Permission extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, nullable = false)
	private String name;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
}
