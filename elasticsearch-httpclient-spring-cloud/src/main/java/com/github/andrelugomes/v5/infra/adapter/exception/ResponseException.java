package com.github.andrelugomes.v5.infra.adapter.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(final String message) {
        super(message);
    }
}
