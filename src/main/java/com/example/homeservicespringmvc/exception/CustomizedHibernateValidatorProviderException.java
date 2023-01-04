package com.example.homeservicespringmvc.exception;

public class CustomizedHibernateValidatorProviderException extends IllegalArgumentException {
    public CustomizedHibernateValidatorProviderException() {
    }

    public CustomizedHibernateValidatorProviderException(String s) {
        super(s);
    }

    public CustomizedHibernateValidatorProviderException(Throwable cause) {
        super(cause);
    }

    public CustomizedHibernateValidatorProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
