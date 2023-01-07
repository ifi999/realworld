package ifi.realworld.user.api;

public interface UserPasswordEncoder {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
