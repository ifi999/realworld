package ifi.realworld.utils.exception.api;

public class UserAuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 4076256550380142367L;

    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
