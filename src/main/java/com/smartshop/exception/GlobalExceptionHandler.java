package com.smartshop.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

   
    private ResponseEntity<ExceptionResponse> buildErrorResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ExceptionResponse response = ExceptionResponse.builder()
                .message(message)
                .dateException(LocalDateTime.now())
                .httpStatus(status)
                .path(request.getRequestURI())
                .httpCode(status.value())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    // ------------ Personalized Excetion

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
            UserNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(
            EntityNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(
            InvalidCredentialsException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DuplicateCredentialsExcception.class)
    public ResponseEntity<ExceptionResponse> handleUsernameDuplicateException(
            DuplicateCredentialsExcception exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorized(
            UnauthorizedException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(
            ForbiddenException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyLoggedIn(
            AlreadyLoggedInException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(StockInsuffisantException.class)
    public ResponseEntity<ExceptionResponse> handleStockInsuffisant(
            StockInsuffisantException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // --------------- Sql and jpa Exceptions

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponse> handleSQLException(
            SQLException exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "Database error occurred: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "Database constraint violation: " + exception.getMostSpecificCause().getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDataAccessException(
            DataAccessException exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "Database access error: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ExceptionResponse> handlePersistenceException(
            PersistenceException exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "Persistence error: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        String message = "Validation error: " +
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildErrorResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "Constraint violation: " + exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(
            IllegalStateException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // ----------------- Gloable Exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(
            Exception exception,
            HttpServletRequest request) {
        return buildErrorResponse(
                "An unexpected error occurred: " + exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }
}
