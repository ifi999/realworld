package ifi.realworld.common.exception;

public class AlreadyExistedUserException extends DefaultCustomException {

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
