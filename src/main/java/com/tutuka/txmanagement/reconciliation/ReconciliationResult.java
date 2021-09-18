package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.model.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconciliationResult {
    private Record record1;
    private Record record2;
    private MatchingResult matchingResult;

     public BigDecimal getMatchingPercentage() {
        return matchingResult.getMatchingPercentage();
     }

}
