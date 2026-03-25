package com.memcyco.scheduler.api;

import com.memcyco.scheduler.error.ScheduleNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ScheduleNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ScheduleNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException e) {
    var first = e.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
    String msg = first != null ? first.getDefaultMessage() : "Validation error";
    return ResponseEntity.badRequest().body(new ApiError(msg));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException e) {
    return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception e) {
    log.error("Unhandled error", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("Something went wrong. Please try again later."));
  }
}
