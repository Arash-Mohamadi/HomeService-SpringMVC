package com.example.homeservicespringmvc.exception;

public class CustomizedInvalidFormatOrSizeFileException extends RuntimeException    {
    public CustomizedInvalidFormatOrSizeFileException() {
    }

    public CustomizedInvalidFormatOrSizeFileException(String message) {
        super(message);
    }

    public CustomizedInvalidFormatOrSizeFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizedInvalidFormatOrSizeFileException(Throwable cause) {
        super(cause);
    }

    public CustomizedInvalidFormatOrSizeFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
