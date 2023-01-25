package ifi.realworld.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO - 여러개를 만들기는 했는데, 너무 쓸데없이 많이 만든 것 같음. 줄일 방향 생각해보기
    
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

    @ExceptionHandler(AlreadyRegistArticleFavorite.class)
    public ErrorResponse handleAlreadyRegistArticleFavorite() {
        ApiError error = new ApiError(ErrorCode.AlreadyRegistArticleFavorite.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ErrorResponse handleArticleNotFoundException() {
        ApiError error = new ApiError(ErrorCode.ArticleNotFoundException.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(NotFoundArticleFavoriteRelationException.class)
    public ErrorResponse handleNotFoundArticleFavoriteRelationException() {
        ApiError error = new ApiError(ErrorCode.NotFoundArticleFavoriteRelationException.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ErrorResponse handleNotFoundCommentException() {
        ApiError error = new ApiError(ErrorCode.NotFoundCommentException.toString());
        return new ErrorResponse(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException() {
        ApiError error = new ApiError(ErrorCode.UserNotFoundException.toString());
        return new ErrorResponse(error);
    }

}
