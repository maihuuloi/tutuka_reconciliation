package com.tutuka.txmanagement.provider.matcher;

import com.tutuka.txmanagement.provider.model.Record;

public interface RecordMatcher {

    MatchingResult compare(Record record1, Record record2) ;
}
