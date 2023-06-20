package com.wang.scaffold.dto;

import lombok.Data;

import java.util.UUID;

/**
 * 消息
 */
@Data
public final class MessageDTO {

	public static final String TOPIC = "topic";
	public static final String QUEUE = "queue";

	private MessageDTO() {}

	public static final MessageDTO topic(String msgFrom, String title, String body, String picUrl, String route) {
		MessageDTO dto = new MessageDTO();
		dto.setType(TOPIC);
		dto.setMsgFrom(msgFrom);
		dto.setTitle(title);
		dto.setBody(body);
		dto.setPicUrl(picUrl);
		dto.setRoute(route);
		return dto;
	}

	public static final MessageDTO queue(String msgFrom, String msgTo, String title, String body, String picUrl, String route) {
		MessageDTO dto = new MessageDTO();
		dto.setType(QUEUE);
		dto.setMsgFrom(msgFrom);
		dto.setMsgTo(msgTo);
		dto.setTitle(title);
		dto.setBody(body);
		dto.setPicUrl(picUrl);
		dto.setRoute(route);
		return dto;
	}

	/** 来自XX的消息 */
	private String msgFrom;

	/** 发给谁的消息，可为null，null则为topic消息 */
	private String msgTo;

	/** 消息类型topic/queue */
	private String type;

	/** 消息标题(注:与新闻/通知公告标题不是一个概念) */
	private String title;

	/** 消息内容 */
	private String body;

	/** 消息图 */
	private String picUrl;

	/** 点击跳转路由 */
	private String route;

	/**  */
	private Integer id;

	private String msgId = UUID.randomUUID().toString();

	/**
	 * 时间戳
	 */
	public long getSendTime() {
		return System.currentTimeMillis();
	}
}
