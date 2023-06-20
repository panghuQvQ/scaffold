package com.wang.scaffold.sharded.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RolePermissionCacheDao implements ApplicationListener<ContextRefreshedEvent> {

	public static final String KEY_PREFIX = "byx:role";

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

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		refresh();
	}
}
