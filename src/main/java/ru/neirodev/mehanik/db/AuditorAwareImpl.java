package ru.neirodev.mehanik.db;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		try {
			return Optional.of((Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}