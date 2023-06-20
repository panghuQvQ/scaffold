package com.wang.scaffold.user.service.impl;

import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.SystemUserRoleTest;
import com.wang.scaffold.user.auth.LoginUserBuilder;
import com.wang.scaffold.user.dto.UserInfo;
import com.wang.scaffold.user.entity.DeletedUser;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.entity.User;
import com.wang.scaffold.user.repository.DeletedUserRepo;
import com.wang.scaffold.user.repository.RoleRepo;
import com.wang.scaffold.user.repository.UserRepo;
import com.wang.scaffold.user.request.UpdateUserStatusRequest;
import com.wang.scaffold.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired private DeletedUserRepo deletedUserRepo;

    @Transactional
    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> op = userRepo.findByUsername(username);
        if (op.isPresent()) {
            LoginUser loginUser = LoginUserBuilder.from(op.get()).build();
            return loginUser;
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    @Transactional
    @Override
    public LoginUser loadUserByPhone(String phone) {
        Optional<User> op = userRepo.findByPhone(phone);
        if (op.isPresent()) {
            LoginUser loginUser = LoginUserBuilder.from(op.get()).build();
            return loginUser;
        } else {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAllWithRole();
    }

    @Transactional
    @Override
    public void configRoles(Integer userId, List<Role> roles) {
        User user = userRepo.findById(userId).get();
        user.replaceRoles(roles);
        userRepo.save(user);
        this.roleProtection(user);
    }

    @Transactional
    @Override
    public User save(User user) {
        if (user.getId() != null) {
            User userDB = userRepo.findById(user.getId()).get();
            userDB.setDisabled(user.isDisabled());
            userDB.setName(user.getName());
            if (StringUtils.hasText(user.getPassword())) {
                userDB.setPassword(user.getPassword());
            }
            userDB.replaceRoles(user.getRoles());
            userRepo.save(userDB);
            this.roleProtection(userDB);
            return userDB;
        } else {
            Optional<User> resUser = userRepo.findByUsername(user.getUsername());
            if (resUser.isPresent()) {
                throw new DuplicateKeyException("登录名已存在");
            }
            deletedUserRepo.findByUsername(user.getUsername()).ifPresent(u -> {
                throw new DuplicateKeyException("这是一个曾被使用的用户名，如需重复使用，请联系管理员清理该用户名下产生的数据，以避免数据泄漏。");
            });
            if (StringUtils.isEmpty(user.getPassword())) {
                throw new IllegalArgumentException("密码不能为空");
            }
            userRepo.save(user);
            this.roleProtection(user);
            return user;
        }
    }

    private void roleProtection(User user) {
        String username = WebAppContextHelper.currentUsername();
        if(user.getRoles() == null) return;
        boolean hasSystemRole = user.getRoles().stream().map(
                e -> roleRepo.findById(e.getId()).get()
        ).anyMatch(SystemUserRoleTest::isSystemRole);
        if (SystemUserRoleTest.isSystemUser(username)) {
            if (user.getUsername().equals(username) && !hasSystemRole) {
                throw new IllegalArgumentException();
            }
        } else {
            if (SystemUserRoleTest.isSystemUser(user)) {
                log.error(username + "违规操作，修改系统管理员");
                throw new IllegalArgumentException();
            }
            if (hasSystemRole) {
                log.error(username + "违规操作，将系统角色赋予" + user.getUsername());
                throw new IllegalArgumentException();
            }
        }
    }

    @Transactional
    @Override
    public void delete(Integer userId, String deleteReason) {
        Optional<User> op = userRepo.findById(userId);
        if (op.isPresent()) {
            User user = op.get();
            if (SystemUserRoleTest.isSystemUser(user)) {
                throw new SecurityException("无法删除system账户");
            }
            List<Role> roles = user.getRoles();
            roles.forEach(e -> {
                if (SystemUserRoleTest.isSystemRole(e)) {
                    throw new SecurityException("无法删除系统管理员账号");
                }
            });
            if (WebAppContextHelper.currentUsername().equals(user.getUsername())) {
                throw new SecurityException("无法删除当前用户");
            }
            log.info("删除用户:" + user.getUsername());
            userRepo.delete(user);
            DeletedUser backup = new DeletedUser();
            BeanUtils.copyProperties(user, backup);
            backup.setRoleIds(roles.stream().map(e -> e.getId().toString()).collect(Collectors.joining(",")));
            backup.setDeletedBy(WebAppContextHelper.currentUsername());
            backup.setDeleteReason(deleteReason);
            backup.setDeletedTime(new Date());
            deletedUserRepo.save(backup);
        }
    }

    @Override
    @Transactional
    public void updatePassword(String oldPassword, String newPassword) {
        String username = WebAppContextHelper.currentUsername();
        User user = userRepo.findByUsername(username).get();
        boolean matches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("旧密码不匹配");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void updateStatus(UpdateUserStatusRequest request) {
        User user = userRepo.findById(request.getId()).get();
        String currentUsername = WebAppContextHelper.currentUsername();
        if (currentUsername.equals(user.getUsername())) {
            throw new SecurityException("无法禁用当前用户");
        }
        user.getRoles().forEach(e -> {
            if (SystemUserRoleTest.isSystemRole(e)) {
                throw new SecurityException("无法禁用管理员账号");
            }
        });
        user.setDisabled(request.getStatus());
        userRepo.save(user);
    }

    @Override
    public UserInfo findUserInfo(String username) {
        Optional<User> op = userRepo.findByUsername(username);
        if (op.isPresent()) {
            User user = op.get();
            if (user.isDisabled()) {
                throw new DisabledException("账号已禁用");
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setName(user.getName());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhone(user.getPhone());
            HashSet<String> permSet = new HashSet<String>();
            if (user.getRoles() != null) {
                List<Role> roles = user.getRoles();
                roles.forEach(r -> {
                    r.getPermissions().forEach(p -> {
                        permSet.add(p.getName());
                    });
                });
            }
            userInfo.setPerms(permSet);
            return userInfo;
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    @Override
    public void updateUserDetail(UserInfo userInfo) {
        String currentUsername = WebAppContextHelper.currentUsername();
        if (currentUsername == null | !currentUsername.equals(userInfo.getUsername())) {
            throw new SecurityException("无法修改当前用户信息");
        }
        User user = userRepo.findByUsername(currentUsername).get();
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());
        user.setAvatar(userInfo.getAvatar());
        userRepo.save(user);
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findUserByDisabled(boolean disabled) {
        return userRepo.findUserByDisabled(disabled);
    }


    @Transactional
    public List<String> getUsernameByRoleName(String roleName) {
        String sql = "SELECT username FROM app_user WHERE id in (SELECT user_id FROM app_user_role WHERE role_id in (SELECT id FROM app_role WHERE name = '" + roleName + "'))";
        return jdbcTemplate.queryForList(sql).stream().map(e -> e.get("username").toString()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void checkDefaultUsers() {
        for (SystemUserRoleTest.ImmutableUser iUser : SystemUserRoleTest.getDefaultUsers()) {
            Optional<User> userOptional = userRepo.findByUsername(iUser.getUsername());
            List<Role> roles = iUser.getRoles().stream().map(iRole -> {
                Role role = roleRepo.findByRoleName(iRole.getName());
                return role;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            User user = userOptional.orElse(new User());

            if (user.getId() == null) {
                user.setUsername(iUser.getUsername());
                user.setPassword(iUser.getPassword());
                user.setName(iUser.getName());
                user.setAvatar(iUser.getAvatar());
                user.setPhone(iUser.getPhone());
                user.setEmail(iUser.getEmail());
                user.setDisabled(iUser.isDisabled());
                user.setRoles(roles);
            } else {
                List<String> roleNamesDB = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
                for (Role r : roles) {
                    if (!roleNamesDB.contains(r.getName())) {
                        user.getRoles().add(r);
                    }
                }
            }
            userRepo.save(user);
        }
    }
}
