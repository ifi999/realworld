package ifi.realworld.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DefaultCustomException.class)
    public ResponseEntity<ErrorResponse> handleDefaultCustomException(DefaultCustomException e) {
        ApiError error = new ApiError(e.customExceptionStatus().toString(), e.customExceptionMessage().toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ErrorResponse(error));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = new ApiError(ex.getLocalizedMessage(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ErrorResponse(error));
    }

}
