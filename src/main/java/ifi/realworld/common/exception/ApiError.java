package ifi.realworld.common.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ApiError {

    private List<String> body;

    public ApiError(List<String> errors) {
        body = errors;
    }

    public ApiError(String error) {
        body = Arrays.asList(error);
    }

}
