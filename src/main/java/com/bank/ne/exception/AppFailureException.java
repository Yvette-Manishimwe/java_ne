package com.bank.ne.exception;

import com.bank.ne.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@ControllerAdvice
public class AppFailureException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAnyError(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidations(MethodArgumentNotValidException exception) {
        FieldError error = Objects.requireNonNull(exception.getFieldError());
        String message = error.getField() + ": " + error.getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message, error));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSqlExceptions(org.hibernate.exception.ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage() + " - " + exception.getSQL() + " - " + exception.getSQLState(), exception.getSQLException()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Data integrity violation", exception));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authentication failed", exception));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Access denied", exception));
    }
}







