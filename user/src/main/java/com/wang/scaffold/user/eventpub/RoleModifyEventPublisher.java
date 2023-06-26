package com.wang.scaffold.user.eventpub;

import com.wang.scaffold.sharded.cloudevent.RoleModifyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 角色修改事件发布器
 *
 * 实现事件发布主要需要下面几个对象：
 * 任务：随意的对象
 * 事件：RoleModifyEvent.class
 * 事件监听：RolePermissionCacheDao.class
 */
@Component
public class RoleModifyEventPublisher {

	@Autowired ApplicationEventPublisher applicationEventPublisher;

	@Autowired BusProperties busProperties;

	public void publish() {
		applicationEventPublisher.publishEvent(new RoleModifyEvent(new Object(), busProperties.getId()));
	}
}
