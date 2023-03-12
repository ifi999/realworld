package ifi.realworld.common.exception.api;

import ifi.realworld.common.exception.DefaultCustomException;
import ifi.realworld.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidEmailException extends DefaultCustomException {

    private static final long serialVersionUID = -4715011436700316770L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.INVALID_EMAIL;
    }

    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }
}
