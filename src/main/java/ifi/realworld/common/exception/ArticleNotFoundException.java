package ifi.realworld.common.exception;

public class ArticleNotFoundException extends DefaultCustomException {

    private static final long serialVersionUID = 3866486897545516217L;

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
