package com.github.andrelugomes.v3.infrastructure.adapter.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(final String message) {
        super(message);
    }
}
