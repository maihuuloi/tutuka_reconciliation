package com.tutuka.txmanagement.reconciliation.model;

/**
 * Represent a record to be reconcile from source
 */
public interface Record {

    Object getValueByColumnName(String columnName);

}
