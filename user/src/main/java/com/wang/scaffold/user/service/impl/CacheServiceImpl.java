package com.wang.scaffold.user.service.impl;

import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import com.wang.scaffold.user.entity.Permission;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.eventpub.RoleModifyEventPublisher;
import com.wang.scaffold.user.repository.RoleRepo;
import com.wang.scaffold.user.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CacheServiceImpl implements CacheService {

	@Autowired StringRedisTemplate redisTemplate;
	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired
	RoleRepo roleRepo;
	@Autowired
	RoleModifyEventPublisher roleModifyEventPublisher;

	@Override
	public void cacheRolePermission() {
		Set<String> keys = redisTemplate.keys(RolePermissionCacheDao.KEY_PREFIX + ":*");
		redisTemplate.delete(keys);
		SetOperations<String, String> ops = redisTemplate.opsForSet();
		List<Role> roles = roleRepo.findAllWithPermissions();
		roles.forEach(role -> {
			List<Permission> perms = role.getPermissions();
			List<String> temp = perms.stream().map(Permission::getName).collect(Collectors.toList());
			ops.add(RolePermissionCacheDao.KEY_PREFIX + ":" + role.getId(), temp.toArray(new String[0]));
		});
		redisTemplate.boundValueOps("cache:" + RolePermissionCacheDao.KEY_PREFIX).set(LocalDateTime.now().toString());
		roleModifyEventPublisher.publish();
	}
}
