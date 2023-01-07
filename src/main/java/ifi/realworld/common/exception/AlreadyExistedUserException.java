package ifi.realworld.common.exception;

public class AlreadyExistedUserException extends DefaultCustomException {

    private static final long serialVersionUID = 7317961365126506972L;

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
