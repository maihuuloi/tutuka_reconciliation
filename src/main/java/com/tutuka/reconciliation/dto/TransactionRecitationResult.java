package com.tutuka.reconciliation.dto;

import com.tutuka.reconciliation.provider.MatchingResult;
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
    private TransactionRecord record1;
    private TransactionRecord record2;
    private MatchingResult matchingResult;

     public BigDecimal getMatchingPercentage() {
        return matchingResult.getMatchingPercentage();
     }

}
