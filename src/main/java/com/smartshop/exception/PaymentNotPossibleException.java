package com.smartshop.exception;

public class PaymentNotPossibleException extends RuntimeException {
    public PaymentNotPossibleException(String message) {
        super(message);
    }
}
