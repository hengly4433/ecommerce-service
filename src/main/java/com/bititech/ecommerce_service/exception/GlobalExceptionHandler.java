package com.bititech.ecommerce_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    var body = new ApiError(Instant.now(), 404, "Not Found", ex.getMessage(), req.getRequestURI(), List.of());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBiz(BusinessException ex, HttpServletRequest req) {
    var body = new ApiError(Instant.now(), 422, "Unprocessable Entity", ex.getMessage(), req.getRequestURI(), List.of());
    return ResponseEntity.status(422).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    var details = ex.getBindingResult().getFieldErrors().stream()
        .map(f -> f.getField()+": "+f.getDefaultMessage()).toList();
    var body = new ApiError(Instant.now(), 400, "Bad Request", "Validation failed", req.getRequestURI(), details);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
    var details = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath()+": "+v.getMessage()).toList();
    var body = new ApiError(Instant.now(), 400, "Bad Request", "Constraint violation", req.getRequestURI(), details);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleOthers(Exception ex, HttpServletRequest req) {
    var body = new ApiError(Instant.now(), 500, "Internal Server Error", ex.getMessage(), req.getRequestURI(), List.of());
    return ResponseEntity.status(500).body(body);
  }
}