package com.smartshop.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<String> errors;
    public ValidationException(List<String>  errors) {

        super("Failed Validation");
        this.errors = errors;
    }
}
