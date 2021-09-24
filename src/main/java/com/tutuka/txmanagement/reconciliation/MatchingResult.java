package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.constant.Constants;
import com.tutuka.txmanagement.reconciliation.strategy.RecordMatcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of {@link RecordMatcher} matching process
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingResult {
    private Integer matchingPercentage = null;
    private List<String> unmatchedColumns = new ArrayList<>();

    public static MatchingResult zeroMatching() {
        return new MatchingResult(Constants.ZERO_PERCENTAGE, new ArrayList<>());
    }
}
