package com.tutuka.txmanagement.exception;

public class ColumnNameNotFoundException extends RuntimeException{
    public ColumnNameNotFoundException() {
    }

    public ColumnNameNotFoundException(String message) {
        super(message);
    }
}
