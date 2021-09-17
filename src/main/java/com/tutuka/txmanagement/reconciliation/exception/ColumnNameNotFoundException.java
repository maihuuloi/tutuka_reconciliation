package com.tutuka.txmanagement.reconciliation.exception;

public class ColumnNameNotFoundException extends RuntimeException{
    public ColumnNameNotFoundException() {
    }

    public ColumnNameNotFoundException(String message) {
        super(message);
    }
}
