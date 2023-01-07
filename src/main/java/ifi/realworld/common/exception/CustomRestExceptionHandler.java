package ifi.realworld.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistedUserException.class)
    public ErrorResponse handleAlreadyExistedUserException() {
        ApiError error = new ApiError(ErrorCode.ALREADY_EXISTED_USER.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ErrorResponse handlePasswordNotMatchedException() {
        ApiError error = new ApiError(ErrorCode.PASSWORD_NOT_MATCHED.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ErrorResponse handleInvalidEmailException() {
        ApiError error = new ApiError(ErrorCode.INVALID_EMAIL.toString());
        return new ErrorResponse(error);
    }

}
