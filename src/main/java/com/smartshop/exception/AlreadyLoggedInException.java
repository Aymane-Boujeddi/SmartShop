package com.smartshop.exception;

public class AlreadyLoggedInException extends RuntimeException {
    public AlreadyLoggedInException(String message) {
        super(message);
    }
}
