package ifi.realworld.common.exception;

public class NotFoundCommentException extends DefaultCustomException {

    private static final long serialVersionUID = -4842830797496200138L;

    public NotFoundCommentException() {
        super();
    }

    public NotFoundCommentException(String message) {
        super(message);
    }

    public NotFoundCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCommentException(Throwable cause) {
        super(cause);
    }
}
