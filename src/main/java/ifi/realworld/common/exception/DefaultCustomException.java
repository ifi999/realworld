package ifi.realworld.common.exception;

public abstract class DefaultCustomException extends RuntimeException {

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
