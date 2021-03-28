package com.github.andrelugomes.v2.infrastructure.adapter.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(final String message) {
        super(message);
    }
}
