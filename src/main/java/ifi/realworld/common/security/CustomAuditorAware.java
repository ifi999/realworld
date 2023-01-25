package ifi.realworld.common.security;

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
                    // TODO - 여기 좀 이상함. ROLE "USER", 그리고 원래는 id 값을 넣고싶었음
                    String auth = authentication.getAuthorities().toString();
                    String userName = authentication.getName();
                    if (!auth.contains("ROLE_ANONYMOUS") && !userName.equals("anonymousUser")) {
                        return authentication.getName();
                    }
                    return null;
                });
    }
}
