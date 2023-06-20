package com.wang.scaffold.user.config;


import com.wang.scaffold.sharded.security.JwtAuthorizationFilter;
import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @title 自定义 SpringSecurity 配置类;
 * @description SpringSecurity 要求这个配置类要继承 WebSecurityConfigurerAdapter
 * @author wzy
 * @updateTime 2023/6/20 14:51
 * @throws
 */
@EnableConfigurationProperties(JwtProperties.class) // 作用：使得使用 @ConfigurationProperties 注解的类生效，即将 JwtProperties类注入到Spring 容器中。
@EnableWebSecurity // 启用 Spring Security 所需的各项配置，如下文中的 configure() 方法
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启 Security 注解，来判断用户对某个控制层的方法是否具有访问权限，如 @PreAuthorize
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 不走security的url地址，antPatterns 格式{@link org.springframework.security.web.util.matcher.AntPathRequestMatcher}
	 */
	public static final String[] BYPASS_PATTERNS = {
			"/session/*", // session是token校验，token刷新，登出接口
			"/captcha/*", // 验证码
			"/error",
			"/ws/**"      // websocket endpoint
	};

//	@Autowired private JwtProperties jwtProperties;
//	@Autowired private AuthTokenService authTokenService;
//	@Autowired private UserServiceImpl userDetailsService;
//	@Autowired private PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider;
//	@Autowired private CaptchaInterceptor captchaInterceptor;
//
//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.cors().and()
//		.csrf().disable()
//		.authorizeRequests()
//		.anyRequest().authenticated()
//		.and()
//		.addFilter(this.jwtAuthenticationFilter())
//		.addFilter(this.jwtAuthorizationFilter())
//		.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//	}
//
//	private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager(), jwtProperties.getAuthUrl());
//		filter.setJwtProperties(jwtProperties);
//		filter.setAuthTokenService(authTokenService);
//		// 验证码校验拦截器
////		filter.addPreAuthenInterceptor(captchaInterceptor);
//		// 手机app版本，低版本拦截器
//		filter.addPreAuthenInterceptor(new ClientAppVersionInterceptor());
//		return filter;
//	}
//
//	private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
//		JwtAuthorizationFilter filter = new JwtAuthorizationFilter(authenticationManager());
//		filter.setJwtProperties(jwtProperties);
//		return filter;
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(BYPASS_PATTERNS);
//	}
//
//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//		.userDetailsService(userDetailsService)
//		.passwordEncoder(passwordEncoder());
//		// 添加手机号码登录的验证类
//		auth.authenticationProvider(phoneCodeAuthenticationProvider);
//	}

	/**
	 * @title 注入 BCryptPasswordEncoder
	 * @description 使用 BCryptPasswordEncoder 进行密码加密存储，解密校验
	 * @author wzy
	 * @updateTime 2023/6/20 14:54
	 * @throws
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
