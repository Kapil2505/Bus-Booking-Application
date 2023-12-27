package com.RedBus.Exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorMessage;

    public BlogAPIException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
