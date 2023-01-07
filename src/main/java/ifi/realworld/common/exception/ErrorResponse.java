package ifi.realworld.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private ApiError errors;

    public ErrorResponse(ApiError apiError) {
        errors = apiError;
    }
}
