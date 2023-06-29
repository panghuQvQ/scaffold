package com.wang.scaffold.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@JsonIgnoreProperties({"createdBy", "createdTime", "updatedBy", "updatedTime"})
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_message")
public class Message extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	/** 来自XX的消息 */
	private String msgFrom;

	/** 发给谁的消息，可为null，null则为topic消息 */
	private String msgTo;

	/** 消息标题(注:与新闻/通知公告标题不是一个概念) */
	private String title;

	/** 消息内容 */
	private String body;

	/** 消息图 */
	private String picUrl;

	/** 点击跳转路由 */
	private String route;

	/** 是否已读 */
	private boolean readStatus;

	/**
	 * 时间戳
	 */
	public long getSendTime() {
		return getCreatedTime().getTime();
	}
}
