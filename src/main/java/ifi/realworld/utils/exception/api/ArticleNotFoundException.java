package ifi.realworld.utils.exception.api;

import ifi.realworld.utils.exception.DefaultCustomException;
import ifi.realworld.utils.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ArticleNotFoundException extends DefaultCustomException {

    private static final long serialVersionUID = 3866486897545516217L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.ARTICLE_NOT_FOUND;
    }

    public ArticleNotFoundException() {
        super();
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotFoundException(Throwable cause) {
        super(cause);
    }
}
