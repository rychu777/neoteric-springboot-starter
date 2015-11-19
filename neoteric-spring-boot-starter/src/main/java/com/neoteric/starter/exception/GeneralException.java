package com.neoteric.starter.exception;

public final class GeneralException extends RuntimeException {

    private final int statusCode;

    public GeneralException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
