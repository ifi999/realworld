package ifi.realworld.user.api;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserPasswordEncoder extends PasswordEncoder {
    @Override
    String encode(CharSequence rawPassword);

    @Override
    boolean matches(CharSequence rawPassword, String encodedPassword);

}
