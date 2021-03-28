package com.github.andrelugomes.v4.infrastructure.adapter.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(final String message) {
        super(message);
    }
}
