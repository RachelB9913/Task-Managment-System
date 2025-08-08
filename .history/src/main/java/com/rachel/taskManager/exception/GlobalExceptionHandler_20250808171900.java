
package com.rachel.taskManager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // A global exception handler to catch and format exceptions in a consistent way. 
    // This will handle various exceptions and return a standardized error response.

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(org.springframework.web.server.ResponseStatusException ex) {
        return buildResponse(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason() != null ? ex.getReason() : ex.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(SecurityException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        System.err.println("AccessDeniedException caught: " + ex.getClass().getName() + ": " + ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request parameter");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        System.err.println("Exception caught: " + ex.getClass().getName() + ": " + ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), message);
        return new ResponseEntity<>(response, status);
    }
}
