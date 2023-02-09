package ifi.realworld.common.exception;

public class UserAuthenticationException extends DefaultCustomException {
    private static final long serialVersionUID = 4076256550380142367L;

    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
