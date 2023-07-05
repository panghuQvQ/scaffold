package com.wang.scaffold.files.config;

import com.wang.scaffold.sharded.security.JwtAuthorizationFilter;
import com.wang.scaffold.sharded.security.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableConfigurationProperties(JwtProperties.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtProperties jwtProperties;

	/**
	 * 不走security的url地址，antPatterns 格式{@link org.springframework.security.web.util.matcher.AntPathRequestMatcher}
	 */
	public static final String[] BYPASS_PATTERNS = {"/public/**", "/protected/**", "/static/**", "/error"};

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and()
				.csrf().disable()
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.addFilter(this.jwtAuthorizationFilter())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception{
		JwtAuthorizationFilter filter = new JwtAuthorizationFilter(authenticationManager());
		filter.setJwtProperties(jwtProperties);
		return filter;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(BYPASS_PATTERNS);
	}
}
