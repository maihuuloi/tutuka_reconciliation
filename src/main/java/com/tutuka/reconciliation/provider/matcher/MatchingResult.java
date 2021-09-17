package com.tutuka.reconciliation.provider.matcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingResult {
    private BigDecimal matchingPercentage = null;
    private List<String> unmatchedColumns = new ArrayList<>();

    public static MatchingResult zeroMatching() {
        return new MatchingResult(BigDecimal.ZERO, new ArrayList<>());
    }
}
