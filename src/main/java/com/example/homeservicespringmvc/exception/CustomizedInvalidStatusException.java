package com.example.homeservicespringmvc.exception;

public class CustomizedInvalidStatusException extends RuntimeException{
    public CustomizedInvalidStatusException() {
    }

    public CustomizedInvalidStatusException(String message) {
        super(message);
    }

    public CustomizedInvalidStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedInvalidStatusException(Throwable cause) {
        super(cause);
    }

    public CustomizedInvalidStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
