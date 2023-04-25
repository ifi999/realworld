package ifi.realworld.utils.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class CustomAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    String auth = authentication.getAuthorities().toString();
                    String userName = authentication.getName();
                    if (!auth.contains("ROLE_ANONYMOUS") && !userName.equals("anonymousUser")) {
                        return authentication.getName();
                    }
                    return null;
                });
    }
}
