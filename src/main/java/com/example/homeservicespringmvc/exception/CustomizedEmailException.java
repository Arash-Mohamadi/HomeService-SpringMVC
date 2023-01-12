package com.example.homeservicespringmvc.exception;

public class CustomizedEmailException extends RuntimeException{

    public CustomizedEmailException() {
    }

    public CustomizedEmailException(String message) {
        super(message);
    }

    public CustomizedEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedEmailException(Throwable cause) {
        super(cause);
    }

    public CustomizedEmailException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
