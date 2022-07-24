package com.sotska.entity;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
    METHOD_NOT_ALLOWED(405, "METHOD NOT ALLOWED"),
    BAD_REQUEST(400, "BAD REQUEST");

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
