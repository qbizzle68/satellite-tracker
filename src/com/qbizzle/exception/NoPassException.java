package com.qbizzle.exception;

public class NoPassException extends RuntimeException {

    public NoPassException(String errorMessage) {
        super(errorMessage);
    }
}
