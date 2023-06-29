package com.wang.scaffold.user.config;

import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.sharded.security.token.Constants;
import com.wang.scaffold.sharded.security.token.JwtTokenParser;
import com.wang.scaffold.sharded.security.token.ParsedJwtToken;
import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @title 自定义配置 STOMP 通讯的配置类
 * @description
 * @author wzy
 * @updateTime 2023/6/27 15:54
 * @throws
 */
@Configuration
@EnableWebSocketMessageBroker // 表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理。
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	JwtProperties jwtProperties;

	/**
	 * 这个方法的作用是定义消息代理，通俗一点讲就是设置消息连接请求的各种规范信息。
	 * @param registry
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 自定义调度器，用于控制心跳线程
		ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
		// 线程池线程数，心跳连接开线程
		te.setPoolSize(4);
		// 线程名前缀
		te.setThreadNamePrefix("wss-heartbeat-thread-");
		// 初始化
		te.initialize();
		registry.enableSimpleBroker("/topic","/queue") // 表示客户端订阅地址的前缀信息，也就是客户端接收服务端消息的地址的前缀信息
		.setHeartbeatValue(new long[]{30000, 30000}) // 进行心跳设置，第一值表示server最小能保证发的心跳间隔毫秒数, 第二个值代码server希望client发的心跳间隔毫秒数
		.setTaskScheduler(te); // 可以配置心跳线程调度器
		//		registry.enableStompBrokerRelay("/topic","/queue");

		// "/app" 为配置应用服务器的地址前缀，表示所有以/app 开头的客户端消息或请求都会路由到带有@MessageMapping 注解的方法中
		registry.setApplicationDestinationPrefixes("/app");
	}

	/**
	 * 这个方法的作用是添加一个服务端点，来接收客户端的连接。
	 * @param registry
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws") // 表示添加了一个/ws 端点，客户端就可以通过这个端点来进行连接。
		.setAllowedOrigins("*") // 跨域设置，*表示所有域名都可以，不限制， 域包括ip：port, 指定*可以是任意的域名，不加的话默认localhost+本服务端口
		.withSockJS(); // 开启SockJS支持，当用 postman连接的时候，需要注释掉这一行
	}

	/**
	 * 添加channel拦截器，在STOMP CONNECT阶段认证：
	 * 设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
	 * @param registration
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// 添加stomp自定义拦截器，可以根据业务做一些处理
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor =
						MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				// 判断是否首次连接请求
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					List<String> l = accessor.getNativeHeader("auth_token");
					if (l != null && l.size() > 0) {
						String token = l.get(0);
						List<SimpleGrantedAuthority> authorities = null;

						ParsedJwtToken pjt = JwtTokenParser.useSecret(jwtProperties.getSecret()).parseToken(token);
						String username = pjt.getUsername();
						List<Integer> roleIds = pjt.getClaimList(Constants.GROUPS, o -> {
							return Integer.valueOf(o.toString());
						});
						if (roleIds != null) {
							authorities = new ArrayList<>();
							RolePermissionCacheDao cache = WebAppContextHelper.getBean(RolePermissionCacheDao.class);
							Set<String> perms = cache.getPermsByRole(roleIds);
							for (String perm : perms) {
								authorities.add( new SimpleGrantedAuthority(perm));
							}
						}
						if (authorities == null) {
							List<String> perms = pjt.getClaimList(Constants.PERMS, o -> {
								return o.toString();
							});
							if (perms != null) {
								authorities = perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
							}
						}
						Authentication user = new UsernamePasswordAuthenticationToken(username, null, authorities);
						accessor.setUser(user);
					}
				}
				return message;
			}
		});
	}

}
