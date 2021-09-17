package com.tutuka.txmanagement.dto;

import com.tutuka.txmanagement.provider.RecitationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ReconciliationResultResponse {
    ReconciliationOverviewResponse reconciliationOverview;
    List<RecitationResult> matchingRecords;
    List<RecitationResult> unmatchedRecords;
    List<RecitationResult> suggestedRecords;
}
