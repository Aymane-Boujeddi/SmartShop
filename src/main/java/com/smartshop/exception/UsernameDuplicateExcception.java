package com.smartshop.exception;

public class UsernameDuplicateExcception extends RuntimeException {
    public UsernameDuplicateExcception(String message) {
        super(message);
    }
}
