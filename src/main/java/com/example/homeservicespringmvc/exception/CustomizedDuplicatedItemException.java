package com.example.homeservicespringmvc.exception;

public class CustomizedDuplicatedItemException extends RuntimeException{
    public CustomizedDuplicatedItemException() {
    }

    public CustomizedDuplicatedItemException(String message) {
        super(message);
    }

    public CustomizedDuplicatedItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedDuplicatedItemException(Throwable cause) {
        super(cause);
    }

    public CustomizedDuplicatedItemException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
