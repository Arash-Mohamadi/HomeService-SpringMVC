package com.example.homeservicespringmvc.exception;

public class CustomizedNotFoundException extends RuntimeException{
    public CustomizedNotFoundException() {
    }

    public CustomizedNotFoundException(String message) {
        super(message);
    }

    public CustomizedNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedNotFoundException(Throwable cause) {
        super(cause);
    }

    public CustomizedNotFoundException(String message, Throwable cause, boolean enableSuppression
            , boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
