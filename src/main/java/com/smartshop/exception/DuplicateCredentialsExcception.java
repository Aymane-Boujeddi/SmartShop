package com.smartshop.exception;

public class DuplicateCredentialsExcception extends RuntimeException {
    public DuplicateCredentialsExcception(String message) {
        super(message);
    }
}
