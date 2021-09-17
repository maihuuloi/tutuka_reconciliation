package com.tutuka.reconciliation.dto;

import com.tutuka.reconciliation.provider.MatchingResult;
import com.tutuka.reconciliation.provider.model.Record;
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
public class TransactionRecitationResult {
    private Record record1;
    private Record record2;
    private MatchingResult matchingResult;

     public BigDecimal getMatchingPercentage() {
        return matchingResult.getMatchingPercentage();
     }

}
