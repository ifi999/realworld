package ifi.realworld.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundArticleFavoriteRelationException extends DefaultCustomException {

    private static final long serialVersionUID = -4841340103708466138L;

    @Override
    public HttpStatus customExceptionStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode customExceptionMessage() {
        return ErrorCode.ARTICLE_FAVORITE_RELATION_NOT_FOUND;
    }

    public NotFoundArticleFavoriteRelationException() {
        super();
    }

    public NotFoundArticleFavoriteRelationException(String message) {
        super(message);
    }

    public NotFoundArticleFavoriteRelationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundArticleFavoriteRelationException(Throwable cause) {
        super(cause);
    }
}
