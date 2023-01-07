package ifi.realworld.common.exception;

public class PasswordNotMatchedException extends DefaultCustomException {

    private static final long serialVersionUID = -4841340797703200138L;

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
