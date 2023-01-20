package com.example.config;

import com.example.security.UserPrincipal;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableJpaAuditing
@Configuration
public class SpringDataAuditConfig implements AuditorAware<String> {

    private static final String SYSTEM_USER = "app";

    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(SYSTEM_USER);
        }

        if( authentication.getPrincipal() instanceof String ) {
            return Optional.ofNullable((String)authentication.getPrincipal());
        }else {
            return Optional.ofNullable(((UserPrincipal) authentication.getPrincipal()).getId());
        }
    }
}
