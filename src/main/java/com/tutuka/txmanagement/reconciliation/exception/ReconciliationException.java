package com.tutuka.txmanagement.reconciliation.exception;

public class ReconciliationException extends RuntimeException{
    public ReconciliationException() {
    }

    public ReconciliationException(String message) {
        super(message);
    }

    public ReconciliationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReconciliationException(Throwable cause) {
        super(cause);
    }

    public ReconciliationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
