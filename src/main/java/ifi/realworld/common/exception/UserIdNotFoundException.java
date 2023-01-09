package ifi.realworld.common.exception;

public class UserIdNotFoundException extends DefaultCustomException {

    private static final long serialVersionUID = 4092209550180142394L;

    public UserIdNotFoundException() {
        super();
    }

    public UserIdNotFoundException(String message) {
        super(message);
    }

    public UserIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
