package ifi.realworld.common.exception;

public class NotFoundArticleFavoriteRelation extends DefaultCustomException {
    public NotFoundArticleFavoriteRelation() {
        super();
    }

    public NotFoundArticleFavoriteRelation(String message) {
        super(message);
    }

    public NotFoundArticleFavoriteRelation(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundArticleFavoriteRelation(Throwable cause) {
        super(cause);
    }
}
