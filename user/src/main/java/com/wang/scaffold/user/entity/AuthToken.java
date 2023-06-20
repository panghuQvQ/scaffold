package com.wang.scaffold.user.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/** 用户认证token */
@Data
@Entity
@Table(name = "app_auth_token")
public class AuthToken {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, nullable = false)
	private String username;

	@Column(length = 36, nullable = false, unique = true)
	private String refreshToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expireTime;

	@Column(length = 100)
	private String device;

	private String userAgent;

	public boolean isMobile() {
		if(device != null) {
			if(device.startsWith("IOS") || device.startsWith("ANDROID")) {
				return true;
			}
		}
		return false;
	}
}
