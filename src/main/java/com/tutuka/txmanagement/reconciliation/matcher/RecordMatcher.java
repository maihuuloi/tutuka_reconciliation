package com.tutuka.txmanagement.reconciliation.matcher;

import com.tutuka.txmanagement.reconciliation.model.Record;

public interface RecordMatcher {

    MatchingResult compare(Record record1, Record record2) ;
}
