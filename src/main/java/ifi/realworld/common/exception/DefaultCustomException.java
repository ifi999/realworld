package ifi.realworld.common.exception;

import org.springframework.http.HttpStatus;

public abstract class DefaultCustomException extends RuntimeException {


    public abstract HttpStatus customExceptionStatus();

    public abstract ErrorCode customExceptionMessage();

    public DefaultCustomException() {
        super();
    }

    public DefaultCustomException(String message) {
        super(message);
    }

    public DefaultCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultCustomException(Throwable cause) {
        super(cause);
    }

}
