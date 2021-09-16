package com.tutuka.reconciliation.provider;

import com.tutuka.reconciliation.dto.TransactionRecord;

public interface ReconciliationProvider {

    MatchingResult calculate(TransactionRecord record1, TransactionRecord record2) ;
}
