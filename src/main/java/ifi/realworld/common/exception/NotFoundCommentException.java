package ifi.realworld.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundCommentException extends DefaultCustomException {

    private static final long serialVersionUID = -4842830797496200138L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.COMMENT_NOT_FOUND;
    }

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
