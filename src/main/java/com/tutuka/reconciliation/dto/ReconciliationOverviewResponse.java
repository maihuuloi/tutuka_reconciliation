package com.tutuka.reconciliation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReconciliationOverviewResponse {
    private Integer totalRecord;
    private Integer matchingRecord;
    private Integer unmatchedRecord;
}
