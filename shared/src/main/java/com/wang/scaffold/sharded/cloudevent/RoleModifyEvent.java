package com.wang.scaffold.sharded.cloudevent;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义事件对象：
 * Spring Cloud Bus的事件都继承于RemoteApplicationEvent类
 */
public class RoleModifyEvent extends RemoteApplicationEvent {

	/**
	 * 显示生成 serialVersionUID，作用：
	 * 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体（类）的serialVersionUID进行比较，
	 * 如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常。(InvalidClassException)
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RoleModifyEvent() {
	}

	public RoleModifyEvent(Object source, String originService) {
		this(source, originService, null);
	}

	public RoleModifyEvent(Object source, String originService, String destination) {
		super(source, originService, destination);
	}

}
