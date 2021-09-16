package com.tutuka.reconciliation.provider;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MatchingResult {
    private BigDecimal matchingPercentage = null;
    private List<String> unmatchedColumns = new ArrayList<>();

    public static MatchingResult zeroMatching() {

        return new MatchingResult(BigDecimal.ZERO, new ArrayList<>());
    }
}
