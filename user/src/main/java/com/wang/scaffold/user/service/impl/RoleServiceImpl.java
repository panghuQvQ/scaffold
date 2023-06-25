package com.wang.scaffold.user.service.impl;

import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import com.wang.scaffold.user.SystemUserRoleTest;
import com.wang.scaffold.user.entity.Permission;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.eventpub.RoleModifyEventPublisher;
import com.wang.scaffold.user.repository.PermissionRepo;
import com.wang.scaffold.user.repository.RoleRepo;
import com.wang.scaffold.user.repository.UserRepo;
import com.wang.scaffold.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PermissionRepo permissionRepo;
    @PersistenceContext
    EntityManager em;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RoleModifyEventPublisher pub;

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Transactional
    @Override
    public Role save(Role role) {
        Role result = null;
        if (role.getId() != null) {
            Role roleDB = roleRepo.findById(role.getId()).get();
            if (!SystemUserRoleTest.isSystemRole(roleDB)) {
                roleDB.setName(role.getName());
            }
            roleDB.replacePermissions(role.getPermissions());
            result = roleRepo.save(roleDB);
        } else {
            result = roleRepo.save(role);
        }

        // --- redis缓存和分布式事件发布，通知刷新缓存
        String key = RolePermissionCacheDao.KEY_PREFIX + ":" + result.getId();
        redisTemplate.delete(key);

        List<Permission> perms = role.getPermissions();
        List<String> temp = perms.stream().map(Permission::getName).collect(Collectors.toList());
        if (temp.size() > 0) {
            redisTemplate.boundSetOps(key).add(temp.toArray(new String[0]));
        }
        pub.publish();
        // ---
        return result;
    }

    @Transactional
    @Override
    public void delete(Integer roleId) {
        Optional<Role> op = roleRepo.findById(roleId);
        if (op.isPresent()) {
            Role role = op.get();
            if (SystemUserRoleTest.isSystemRole(role)) {
                throw new SecurityException("无法删除系统管理员角色");
            }
            Query query = em.createNativeQuery("DELETE FROM app_user_role WHERE role_id = :roleId");
            query.setParameter("roleId", roleId);
            query.executeUpdate();
            roleRepo.delete(op.get());

            // --- redis缓存和分布式事件发布，通知刷新缓存
            String key = RolePermissionCacheDao.KEY_PREFIX + ":" + role.getId();
            redisTemplate.delete(key);
            pub.publish();
            // ---
        }
    }

    @Transactional
    @Override
    public void checkDefaultRoles() {
        for (SystemUserRoleTest.ImmutableRole iRole : SystemUserRoleTest.getDefaultRoles()) {
            Role role = roleRepo.findByRoleName(iRole.getName());
            List<Permission> perms = iRole.getPermissions().stream().map(iPerm -> {
                Permission perm = new Permission();
                perm.setName(iPerm.getName());
                return perm;
            }).collect(Collectors.toList());

            if (role == null) {
                role = new Role();
                role.setName(iRole.getName());
                role.setPermissions(perms);
            } else {
                List<String> permDB = role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
                for (Permission p : perms) {
                    if (!permDB.contains(p.getName())) {
                        role.addPermission(p);
                    }
                }
            }
            roleRepo.save(role);
        }
    }

}
