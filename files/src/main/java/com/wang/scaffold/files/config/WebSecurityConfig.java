package com.wang.scaffold.files.config;

import com.wang.scaffold.sharded.security.JwtAuthorizationFilter;
import com.wang.scaffold.sharded.security.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableConfigurationProperties(JwtProperties.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig {

	@Autowired
	JwtProperties jwtProperties;

	/**
	 * 不走security的url地址，antPatterns 格式{@link org.springframework.security.web.util.matcher.AntPathRequestMatcher}
	 */
	public static final String[] BYPASS_PATTERNS = {"/public/**", "/protected/**", "/static/**", "/error"};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers(BYPASS_PATTERNS);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors().and()
				.csrf().disable()
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.addFilter(this.jwtAuthorizationFilter())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	private JwtAuthorizationFilter jwtAuthorizationFilter() {
		JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
		filter.setJwtProperties(jwtProperties);
		return filter;
	}
}
