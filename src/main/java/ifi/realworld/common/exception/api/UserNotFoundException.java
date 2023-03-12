package ifi.realworld.common.exception.api;

import ifi.realworld.common.exception.DefaultCustomException;
import ifi.realworld.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DefaultCustomException {

    private static final long serialVersionUID = 4092209550180142394L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.USER_NOT_FOUND;
    }

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
