package ifi.realworld.common.exception;

public class NotFoundArticleFavoriteRelationException extends DefaultCustomException {

    private static final long serialVersionUID = -4841340103708466138L;

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
