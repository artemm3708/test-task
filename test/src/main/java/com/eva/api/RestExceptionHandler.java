package com.eva.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.eva.api.dto.response.ErrorResponse;
import com.eva.exception.NotFoundException;
import com.eva.exception.NotValidFilter;
import java.util.regex.PatternSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

  public static final int ERROR_CODE_FIELD_VALIDATION_FAILED = 402;

  @ExceptionHandler(value = {NotFoundException.class})
  public ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
    String error = e.getMessage();
    log.info(error);
    return error(NOT_FOUND, NOT_FOUND.value(), error);
  }

  @ExceptionHandler(value = {NotValidFilter.class})
  public ResponseEntity<ErrorResponse> handleException(NotValidFilter e) {
    String error = e.getMessage();
    log.info(error);
    return error(BAD_REQUEST, ERROR_CODE_FIELD_VALIDATION_FAILED, error);
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    final String message = e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + "=" + error.getRejectedValue() + ", " + error.getDefaultMessage())
        .findFirst()
        .orElse("Argument Not Valid");
    log.info(message);
    return error(BAD_REQUEST, ERROR_CODE_FIELD_VALIDATION_FAILED, message);
  }

  @ExceptionHandler(value = {PatternSyntaxException.class})
  public ResponseEntity<ErrorResponse> handleException(PatternSyntaxException e) {
    final String message = e.getMessage();
    log.info(message);
    return error(BAD_REQUEST, ERROR_CODE_FIELD_VALIDATION_FAILED, message);
  }

  private ResponseEntity<ErrorResponse> error(HttpStatus status, int code, String message) {
    final ErrorResponse response = new ErrorResponse(status.value(), code, message);
    log.info("Error response: {}", response);
    return ResponseEntity
        .status(status)
        .header("Content-Type", "application/json")
        .body(response);
  }
}
