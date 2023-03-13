package ifi.realworld.common.exception.api;

import ifi.realworld.common.exception.DefaultCustomException;
import ifi.realworld.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyRegistArticleFavoriteException extends DefaultCustomException {

    private static final long serialVersionUID = 4071256258384569360L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.ALREADY_REGIST_ARTICLE_FAVORITE;
    }

    public AlreadyRegistArticleFavoriteException() {
        super();
    }

    public AlreadyRegistArticleFavoriteException(String message) {
        super(message);
    }

    public AlreadyRegistArticleFavoriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRegistArticleFavoriteException(Throwable cause) {
        super(cause);
    }
}
