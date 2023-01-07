package ifi.realworld.common.exception;

public class AleayExistedUserExcpetion extends DefaultCustomException {

    public AleayExistedUserExcpetion() {
        super();
    }

    public AleayExistedUserExcpetion(String message) {
        super("This " + message + " already exists.");
    }

    public AleayExistedUserExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public AleayExistedUserExcpetion(Throwable cause) {
        super(cause);
    }
}
