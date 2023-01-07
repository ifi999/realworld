package ifi.realworld.user.api;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class DefaultUserPasswordEncoder implements UserPasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) throw new IllegalArgumentException();

        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) throw new IllegalArgumentException();
        if (encodedPassword == null || encodedPassword.length() == 0) return false;

        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
