package com.wang.scaffold.user.listener;

import com.wang.scaffold.sharded.cloudevent.RoleModifyEvent;
import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置自定义对象：将 RoleModifyEvent 加载到Bus的上下文中
 */
@RemoteApplicationEventScan({"com.wang.scaffold.shared.cloudevent"})
@Configuration
public class CloudEventListeners {

	@Autowired
	RolePermissionCacheDao rolePermissionCacheDao;

	@Bean
	public ApplicationListener<RoleModifyEvent> roleModifyListener() {
		return event -> {
			rolePermissionCacheDao.refresh();
		};
	}

}
