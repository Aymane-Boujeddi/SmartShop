package com.smartshop.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .path(request.getRequestURI())
                .httpCode(401)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(InvalidCredentialsException exception,HttpServletRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .httpCode(400)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicateCredentialsExcception.class)
    public ResponseEntity<ExceptionResponse> handleUsernameDuplicateException(DuplicateCredentialsExcception exception,HttpServletRequest request){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .httpCode(400)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorized(UnauthorizedException exception,HttpServletRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .path(request.getRequestURI())
                .httpCode(401)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(ForbiddenException exception,HttpServletRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateException(LocalDateTime.now())
                .httpStatus(HttpStatus.FORBIDDEN)
                .path(request.getRequestURI())
                .httpCode(403)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
