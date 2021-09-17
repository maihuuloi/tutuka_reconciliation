package com.tutuka.reconciliation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ReconciliationResultResponse {
    ReconciliationOverviewResponse reconciliationOverview;
    List<TransactionRecitationResult> matchingRecords;
    List<TransactionRecitationResult> unmatchedRecords;
    List<TransactionRecitationResult> suggestedRecords;
}
