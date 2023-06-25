package com.wang.scaffold.user;

import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import com.wang.scaffold.user.service.CacheService;
import com.wang.scaffold.user.service.RoleService;
import com.wang.scaffold.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName UserApplication.java
 * @Description TODO
 * @createTime 2023年06月16日 10:17:00
 */
@EnableAsync
@EnableAspectJAutoProxy
@EnableRedisRepositories(basePackages = {"com.wang.scaffold.user.redisrepo"})
@EnableJpaRepositories(basePackages = {"com.wang.scaffold.user.repository"})
@EnableWebSecurity(debug = true) // 开启 测试
@SpringBootApplication(scanBasePackages = "com.wang.scaffold")
@EntityScan(basePackages = {"com.wang.scaffold.user.entity","com.wang.scaffold.entity.jpa"})
public class UserApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CacheService cacheService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            roleService.checkDefaultRoles();
            userService.checkDefaultUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long rolePermsCache = redisTemplate.getExpire("cache:" + RolePermissionCacheDao.KEY_PREFIX);
//		if (rolePermsCache == -2) {
        cacheService.cacheRolePermission();
//		}
    }
}
