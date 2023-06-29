package com.wang.scaffold.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @title 自定义 WebSocketSecurity 配置类，集成Spring Security
 * @description 依赖中引入spring-boot-starter-security和spring-security-messaging
 * @author wzy
 * @updateTime 2023/6/27 16:08
 * @throws
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
		// 所有除了MESSAGE或SUBSCRIBE外的请求都需要通过了验证
		.nullDestMatcher().authenticated()
		// 所有用户都可以订阅
//		.simpSubscribeDestMatchers("/user/queue/errors").permitAll()
		// 订阅这些地址需要相应的权限
//		.simpSubscribeDestMatchers("/topic/admin/**").hasRole("ADMIN")
//		.simpSubscribeDestMatchers("/user/**", "/topic/**").hasRole("USER")
		// 客户端往这个地址发消息时，需要相应的角色权限
//		.simpDestMatchers("/app/admin/**").hasRole("ADMIN")
		// 其他任何未匹配的消息都拒绝
//		.anyMessage().denyAll();
		;
	}

	/**
	 * 是否允许非同源访问，默认为false不允许，会添加CSRF处理
	 * @return
	 */
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

}
