package com.example.homeservicespringmvc.exception;

public class CustomizedFormatOrSizeFileException extends RuntimeException    {
    public CustomizedFormatOrSizeFileException() {
    }

    public CustomizedFormatOrSizeFileException(String message) {
        super(message);
    }

    public CustomizedFormatOrSizeFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedFormatOrSizeFileException(Throwable cause) {
        super(cause);
    }

    public CustomizedFormatOrSizeFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
