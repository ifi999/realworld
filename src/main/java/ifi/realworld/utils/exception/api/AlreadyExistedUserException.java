package ifi.realworld.utils.exception.api;

import ifi.realworld.utils.exception.DefaultCustomException;
import ifi.realworld.utils.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistedUserException extends DefaultCustomException {

    private static final long serialVersionUID = 7317961365126506972L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.ALREADY_EXISTED_USER;
    }

    public AlreadyExistedUserException() {
        super();
    }

    public AlreadyExistedUserException(String message) {
        super("This " + message + " already exists.");
    }

    public AlreadyExistedUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistedUserException(Throwable cause) {
        super(cause);
    }
}
