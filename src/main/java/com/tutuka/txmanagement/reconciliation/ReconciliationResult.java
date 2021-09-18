package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.model.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconciliationResult {
    //private Record record1; RestTemplate cant map from json to interface
    // , temporary define field type as Object to reuse this class in test
    //private Record record2;

    private Object record1;
    private Object record2;
    private MatchingResult matchingResult;

    public BigDecimal getMatchingPercentage() {
        return matchingResult.getMatchingPercentage();
    }

}
