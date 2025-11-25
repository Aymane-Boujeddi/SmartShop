package com.smartshop.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .stackTrace(exception.getStackTrace())
                .httpCode(401)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(InvalidCredentialsException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .httpCode(400)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicateCredentialsExcception.class)
    public ResponseEntity<ExceptionResponse> handleUsernameDuplicateException(DuplicateCredentialsExcception exception){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .httpCode(400)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
