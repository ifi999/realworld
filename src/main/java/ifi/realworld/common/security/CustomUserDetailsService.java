package ifi.realworld.common.security;

import ifi.realworld.user.domain.User;
import ifi.realworld.user.domain.repository.UserRepository;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        final User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id + " not found."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!principal.equals("anonymousUser")) {
            return (UserDetails) principal;
        }
        throw new SecurityException("May be invalid JWT or not authenticated user.");
    }

    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationServiceException("Not authenticated user.");
        }
        return authentication;
    }
}
