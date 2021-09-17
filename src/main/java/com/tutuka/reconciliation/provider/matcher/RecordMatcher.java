package com.tutuka.reconciliation.provider.matcher;

import com.tutuka.reconciliation.provider.MatchingResult;
import com.tutuka.reconciliation.provider.model.Record;

public interface RecordMatcher {

    MatchingResult compare(Record record1, Record record2) ;
}
