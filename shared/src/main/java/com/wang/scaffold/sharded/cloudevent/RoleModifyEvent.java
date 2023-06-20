package com.wang.scaffold.sharded.cloudevent;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class RoleModifyEvent extends RemoteApplicationEvent {

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
