package com.tutuka.txmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconciliationSummaryResponse {
    private Integer file1TotalCount;
    private Integer file1MatchingCount;
    private Integer file1UnmatchedCount;
    private Integer file1SuggestedCount;

    private Integer file2TotalCount;
    private Integer file2MatchingCount;
    private Integer file2UnmatchedCount;
    private Integer file2SuggestedCount;
}
