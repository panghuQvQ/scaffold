package com.wang.scaffold.sharded.helper;

import com.wang.scaffold.sharded.security.ClientInfoUtil;
import com.wang.scaffold.sharded.user.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * Context帮助类
 * @author zhou wei
 *
 */
@Slf4j
@Component
public class WebAppContextHelper implements ServletContextAware, ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		WebAppContextHelper.servletContext = servletContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		WebAppContextHelper.applicationContext = applicationContext;
	}

	public static UserDetails currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		Object user = authentication.getPrincipal();
		if (user instanceof UserDetails) {
			return (UserDetails) user;
		}
		if (user instanceof String) {
			return org.springframework.security.core.userdetails.User
					.withUsername(user.toString())
					.password(authentication.getCredentials() == null ? "" : authentication.getCredentials().toString())
					.authorities(authentication.getAuthorities())
					.build();
		}
		return null;
	}

	/**
	 * 当前SecurityContext的username，可能为null
	 * @return
	 */
	public static String currentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		Object user = authentication.getPrincipal();
		if (user instanceof UserDetails) {
			return ((UserDetails) user).getUsername();
		}
		if (user instanceof String) {
			return user.toString();
		}
		return null;
	}

	public static ClientInfo currentClientInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication == null ? null : authentication.getDetails();
		if(details instanceof ClientInfo) {
			return (ClientInfo) details;
		} else {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attributes != null) {
				HttpServletRequest request = attributes.getRequest();
				return ClientInfoUtil.extraClientInfo(request);
			}
		}
		return null;
	}

	public static String getServletContextPath() {
		if (servletContext != null) {
			return servletContext.getContextPath();
		}else {
			log.error("Lose ServletContext!");
			return "";
		}
	}

	public static String getAppProperty(String key) {
		return applicationContext.getEnvironment().getProperty(key);
	}

	public static final <T> T getBean(Class<T> clazz) {
		if (applicationContext != null) {
			return applicationContext.getBean(clazz);
		}else {
			log.error("Lose Spring ApplicationContext!");
			return null;
		}
	}

	@PreDestroy
    public void shutdown() {
		WebAppContextHelper.applicationContext = null;
		WebAppContextHelper.servletContext = null;
    }
}
