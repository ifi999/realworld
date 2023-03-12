package ifi.realworld.common.exception.api;

import ifi.realworld.common.exception.DefaultCustomException;
import ifi.realworld.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyRegistArticleFavorite extends DefaultCustomException {

    private static final long serialVersionUID = 4071256258384569360L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.ALREADY_REGIST_ARTICLE_FAVORITE;
    }

    public AlreadyRegistArticleFavorite() {
        super();
    }

    public AlreadyRegistArticleFavorite(String message) {
        super(message);
    }

    public AlreadyRegistArticleFavorite(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRegistArticleFavorite(Throwable cause) {
        super(cause);
    }
}
