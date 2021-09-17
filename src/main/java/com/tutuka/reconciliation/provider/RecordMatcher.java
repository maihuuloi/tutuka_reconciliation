package com.tutuka.reconciliation.provider;

public interface RecordMatcher {

    MatchingResult calculate(TransactionRecord record1, TransactionRecord record2) ;
}
