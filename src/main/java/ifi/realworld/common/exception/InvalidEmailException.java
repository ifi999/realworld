package ifi.realworld.common.exception;

public class InvalidEmailException extends DefaultCustomException {

    private static final long serialVersionUID = -4715011436700316770L;

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
