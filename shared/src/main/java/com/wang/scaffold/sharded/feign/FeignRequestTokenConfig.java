package com.wang.scaffold.sharded.feign;

import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.sharded.security.token.Constants;
import com.wang.scaffold.sharded.security.token.JwtTokenGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConditionalOnClass({RequestInterceptor.class, FeignAutoConfiguration.class})
@Configuration
public class FeignRequestTokenConfig implements RequestInterceptor {

	private JwtProperties jwtProperties;
	private Environment environment;

	public FeignRequestTokenConfig(JwtProperties jwtProperties, Environment environment) {
		this.jwtProperties = jwtProperties;
		this.environment = environment;
	}

	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 如果不为null,则请求由用户发起;如果为null,则请求来自定时任务之类的服务端自发请求.
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			template.header("Authorization", request.getHeader("Authorization"));

		} else {
			String appName = environment.getProperty("spring.application.name", "undefined");
			String username = "@SERVICE-" + appName;
			List<String> perms = Arrays.asList("@service-client");

			Map<String, List<?>> claims = new HashMap<>();
			claims.put(Constants.PERMS, perms);
			String token = JwtTokenGenerator.withProperties(jwtProperties).generateToken(username, claims);
			template.header("Authorization", "Bearer " + token);
		}
	}

}
