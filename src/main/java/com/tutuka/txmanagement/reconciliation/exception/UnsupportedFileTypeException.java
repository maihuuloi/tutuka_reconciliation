package com.tutuka.txmanagement.reconciliation.exception;

public class UnsupportedFileTypeException extends RuntimeException{
    public UnsupportedFileTypeException() {
    }

    public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFileTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedFileTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
