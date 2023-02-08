package ifi.realworld.common.exception;

public class AlreadyRegistArticleFavorite extends DefaultCustomException {

    private static final long serialVersionUID = 4071256258384569360L;

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
