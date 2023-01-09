package ifi.realworld.common.exception;

public class UserNotFoundException extends DefaultCustomException {

    private static final long serialVersionUID = 4092209550180142394L;

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
