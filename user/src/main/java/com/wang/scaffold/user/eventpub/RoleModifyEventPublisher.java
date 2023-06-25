package com.wang.scaffold.user.eventpub;

import com.wang.scaffold.sharded.cloudevent.RoleModifyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class RoleModifyEventPublisher {

	@Autowired ApplicationEventPublisher applicationEventPublisher;

	@Autowired BusProperties busProperties;

	public void publish() {
		applicationEventPublisher.publishEvent(new RoleModifyEvent(new Object(), busProperties.getId()));
	}
}
