package com.finpro.roomio_backends.exceptions;

import com.finpro.roomio_backends.exceptions.user.DuplicateCredentialsException;
import com.finpro.roomio_backends.exceptions.user.UserNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
   private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

   static {
      EXCEPTION_STATUS_MAP.put(DuplicateCredentialsException.class, HttpStatus.BAD_REQUEST);
      EXCEPTION_STATUS_MAP.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
      EXCEPTION_STATUS_MAP.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      ErrorResponse errorResponse = new ErrorResponse(
              LocalDateTime.now(),
              HttpStatus.BAD_REQUEST.value(),
              "Validation Failed",
              "Validation failed for one or more fields",
              errors.toString()
      );
      log.error("Validation exception: ", ex);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleException(Exception ex) {
      HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
      ErrorResponse errorResponse = new ErrorResponse(
              LocalDateTime.now(),
              status.value(),
              ex.getClass().getSimpleName(),
              ex.getMessage(),
              ex.getStackTrace().length > 0 ? ex.getStackTrace()[0].toString() : "No stack trace available"
      );
      log.error("Exception: ", ex);
      return new ResponseEntity<>(errorResponse, status);
   }

   @Data
   public static class ErrorResponse {
      private LocalDateTime timestamp;
      private int status;
      private String error;
      private String message;
      private String trace;

      public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String trace) {
         this.timestamp = timestamp;
         this.status = status;
         this.error = error;
         this.message = message;
         this.trace = trace;
      }
   }
}