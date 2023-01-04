package com.example.homeservicespringmvc.exception;

public class CustomizedDuplicateItemException extends RuntimeException{
    public CustomizedDuplicateItemException() {
    }

    public CustomizedDuplicateItemException(String message) {
        super(message);
    }

    public CustomizedDuplicateItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedDuplicateItemException(Throwable cause) {
        super(cause);
    }

    public CustomizedDuplicateItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
