package ru.neirodev.mehanik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.neirodev.mehanik.db.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class AuditEntityConfig {

	@Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }

}