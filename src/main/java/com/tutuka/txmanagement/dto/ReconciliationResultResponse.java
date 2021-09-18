package com.tutuka.txmanagement.dto;

import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ReconciliationResultResponse {
    ReconciliationSummaryResponse reconciliationOverview;
    List<ReconciliationResult> matchingRecords;
    List<ReconciliationResult> unmatchedRecords;
    List<ReconciliationResult> suggestedRecords;
}
