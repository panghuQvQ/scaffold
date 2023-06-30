package com.wang.scaffold.sharded.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 自定义事件订阅者(观察者)
 *
 * ApplicationContext事件机制是观察者设计模式的实现，通过ApplicationEvent类和ApplicationListener接口，可以实现ApplicationContext事件处理。
 * 实现ApplicationListener<ContextRefreshedEvent>接口可以收到监听动作，然后可以写自己的逻辑。
 *
 * ContextRefreshedEvent：ApplicationContext 被初始化或刷新时，该事件被发布。
 * 此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用
 */
@Component
public class RolePermissionCacheDao implements ApplicationListener<ContextRefreshedEvent> {

	public static final String KEY_PREFIX = "scaffold:role";

	private final Map<String, Set<String>> rolePerms = new HashMap<>();

	@Autowired StringRedisTemplate redisTemplate;

	public Set<String> getPermsByRole(Integer ...roleIds) {
		Set<String> perms = new HashSet<>();
		for (Integer roleId : roleIds) {
			Set<String> temp = rolePerms.get(KEY_PREFIX + ":" + roleId);
			if (temp != null) {
				perms.addAll(temp);
			}
		}
		return perms;
	}

	public Set<String> getPermsByRole(List<Integer> roleIds) {
		return getPermsByRole(roleIds.toArray(new Integer[0]));
	}

	public synchronized void refresh() {
		rolePerms.clear();
		Set<String> keys = redisTemplate.keys(KEY_PREFIX + ":*");
		SetOperations<String, String> ops = redisTemplate.opsForSet();
		keys.forEach(key -> {
			Set<String> perms = ops.members(key);
			rolePerms.put(key, perms);
		});
	}

	/**
	 * 监听到 ApplicationContext 被初始化或刷新时，调用该方法
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		refresh();
	}
}
