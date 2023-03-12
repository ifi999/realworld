package ifi.realworld.common.exception;

import ifi.realworld.common.exception.ApiError;
import ifi.realworld.common.exception.DefaultCustomException;
import ifi.realworld.common.exception.ErrorResponse;
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = new ApiError(ex.getLocalizedMessage(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ErrorResponse(error));
    }
    @ExceptionHandler(DefaultCustomException.class)
    public ResponseEntity<ErrorResponse> handleDefaultCustomException(DefaultCustomException e) {
        ApiError error = new ApiError(e.customExceptionStatus().toString(), e.customExceptionMessage().toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ErrorResponse(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ApiError error = new ApiError(e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(error));
    }

}
