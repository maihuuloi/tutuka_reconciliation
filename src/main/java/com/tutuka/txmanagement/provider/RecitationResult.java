package com.tutuka.txmanagement.provider;

import com.tutuka.txmanagement.provider.matcher.MatchingResult;
import com.tutuka.txmanagement.provider.model.Record;
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
public class RecitationResult {
    private Record record1;
    private Record record2;
    private MatchingResult matchingResult;

     public BigDecimal getMatchingPercentage() {
        return matchingResult.getMatchingPercentage();
     }

}
