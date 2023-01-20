package ifi.realworld.common.exception;

public class NotFoundArticleFavoriteRelationException extends DefaultCustomException {
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
