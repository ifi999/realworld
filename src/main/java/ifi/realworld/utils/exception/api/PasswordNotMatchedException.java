package ifi.realworld.utils.exception.api;

import ifi.realworld.utils.exception.DefaultCustomException;
import ifi.realworld.utils.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends DefaultCustomException {

    private static final long serialVersionUID = -4841340797703200138L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.PASSWORD_NOT_MATCHED;
    }

    public PasswordNotMatchedException() {
        super();
    }

    public PasswordNotMatchedException(String message) {
        super(message);
    }

    public PasswordNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotMatchedException(Throwable cause) {
        super(cause);
    }
}
