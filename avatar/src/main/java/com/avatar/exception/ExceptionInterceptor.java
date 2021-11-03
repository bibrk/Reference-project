package com.avatar.exception;

import java.time.LocalDate;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserAlreadyExistException.class)
  public final ResponseEntity<Object> handleUserAlreadyExist(UserAlreadyExistException ex) {
    ErrorDetails exceptionResponse =
        new ErrorDetails(LocalDate.now(),
            ex.getMessage(), ex.getDetails());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
  }
  
  @ExceptionHandler(UserNotFoundException.class)
  public final ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
    ErrorDetails exceptionResponse =
        new ErrorDetails(LocalDate.now(),
            ex.getMessage(), ex.getDetails());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }

}