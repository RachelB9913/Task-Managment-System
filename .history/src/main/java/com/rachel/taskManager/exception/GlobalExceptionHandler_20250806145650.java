package com.rachel.taskManager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.rachel.taskManager.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(SecurityException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request parameter");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), message);
        return new ResponseEntity<>(response, status);
    }
}
