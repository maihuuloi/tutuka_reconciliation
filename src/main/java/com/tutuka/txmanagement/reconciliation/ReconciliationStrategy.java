package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.model.Record;

import java.util.List;

/**
 * Interface for reconciliation strategy implementation
 */
public interface ReconciliationStrategy {
    List<ReconciliationResult> reconcile(List<Record> source1Records, List<Record> source2Records);
}
