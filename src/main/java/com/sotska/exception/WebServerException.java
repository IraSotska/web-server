package com.sotska.exception;

import com.sotska.entity.HttpStatus;

public class WebServerException extends RuntimeException {

    private HttpStatus httpStatus;
    private Throwable cause;

    public WebServerException(Throwable cause, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
