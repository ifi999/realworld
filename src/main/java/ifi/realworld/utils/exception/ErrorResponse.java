package ifi.realworld.utils.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {

    private ApiError errors;

    public ErrorResponse(ApiError apiError) {
        errors = apiError;
    }
}
