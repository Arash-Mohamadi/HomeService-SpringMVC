package com.example.homeservicespringmvc.exception.config;

import com.example.homeservicespringmvc.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(value = CustomizedDuplicatedItemException.class)
    public ResponseEntity<Object> handleCustomizedDuplicatedItemException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = CustomizedInvalidFormatOrSizeFileException.class)
    public ResponseEntity<Object> handleCustomizedFormatOrSizeFileException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = CustomizedHibernateValidatorProviderException.class)
    public ResponseEntity<Object> handleCustomizedIllegalArgumentException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = CustomizedInvalidStatusException.class)
    public ResponseEntity<Object> handleCustomizedInvalidStatusException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = CustomizedNotFoundException.class)
    public ResponseEntity<Object> handleCustomizedNotFoundException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
