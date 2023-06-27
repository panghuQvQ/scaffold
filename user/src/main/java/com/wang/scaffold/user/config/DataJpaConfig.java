package com.wang.scaffold.user.config;

import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * 适配 {@link org.springframework.data.jpa.domain.support.AuditingEntityListener}
 * @author zhou wei
 */
@Configuration
@EnableJpaAuditing
public class DataJpaConfig {

	@Bean
	public AuditorAware<String> auditorAware() {
		AuditorAware<String> aa = () -> {
			String username = WebAppContextHelper.currentUsername();
			if (username == null) {
				return Optional.empty();
			}
			return Optional.of(username);
		};
		return aa;
	}

}
