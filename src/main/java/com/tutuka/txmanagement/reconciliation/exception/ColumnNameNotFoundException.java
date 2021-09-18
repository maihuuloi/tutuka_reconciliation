package com.tutuka.txmanagement.reconciliation.exception;

/**
 * This exception will be thrown when try to get field value in record
 * with a none exist field name defined by {@link com.tutuka.txmanagement.reconciliation.annotation.MatchColumnName}
 */
public class ColumnNameNotFoundException extends RuntimeException{
    public ColumnNameNotFoundException() {
    }

    public ColumnNameNotFoundException(String message) {
        super(message);
    }
}
