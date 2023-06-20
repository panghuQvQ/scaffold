package com.wang.scaffold.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
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
@SpringBootApplication
@EnableWebSecurity(debug = true)
public class UserApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(UserApplication.class, args);
        System.out.println("1111");
    }
}
