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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 1.新增redis数据：
 * 角色的权限信息
 * 角色的缓存时间
 *
 * 2.发布了事件
 */
@Service
public class CacheServiceImpl implements CacheService {

	@Autowired StringRedisTemplate redisTemplate;
	@Autowired
	RoleRepo roleRepo;
	@Autowired
	RoleModifyEventPublisher roleModifyEventPublisher; // 角色修改事件发布者

	@Override
	public void cacheRolePermission() {
		Set<String> keys = redisTemplate.keys(RolePermissionCacheDao.KEY_PREFIX + ":*");
		redisTemplate.delete(keys);
		SetOperations<String, String> ops = redisTemplate.opsForSet(); // 操作 set
		List<Role> roles = roleRepo.findAllWithPermissions();
		roles.forEach(role -> {
			List<Permission> perms = role.getPermissions();
			List<String> temp = perms.stream().map(Permission::getName).collect(Collectors.toList());
			ops.add(RolePermissionCacheDao.KEY_PREFIX + ":" + role.getId(), temp.toArray(new String[0])); // redis 新增数据，scaffold:role：1 --->角色对应权限信息
		});
		redisTemplate.boundValueOps("cache:" + RolePermissionCacheDao.KEY_PREFIX).set(LocalDateTime.now().toString()); // redis 新增数据，cache:scaffold:role --->角色信息缓存时间
		roleModifyEventPublisher.publish(); // 发布事件
	}
}
