package ifi.realworld.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserPasswordEncoder implements UserPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) throw new IllegalArgumentException();

        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) throw new IllegalArgumentException();
        if (encodedPassword == null || encodedPassword.length() == 0) return false;

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
