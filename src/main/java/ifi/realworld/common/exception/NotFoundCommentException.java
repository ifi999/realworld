package ifi.realworld.common.exception;

public class NotFoundCommentException extends DefaultCustomException {
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
