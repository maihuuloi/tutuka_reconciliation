package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.MatchingResult;
import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.math.BigDecimal;
import java.util.List;

public class RecordMatcher {

    private List<MatchingConfig> passRule;

    public RecordMatcher(List<MatchingConfig> passRule) {
        if (passRule == null || passRule.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.passRule = passRule;
    }

    public MatchingResult compare(Record record1, Record record2) {

        MatchingResult matchingResult = new MatchingResult();
        Integer matchScore = 0;
        for (MatchingConfig matchingConfig : this.passRule) {
            Object value1 = record1.getValueByColumnName(matchingConfig.getColumnName());
            Object value2 = record2.getValueByColumnName(matchingConfig.getColumnName());
            ValueMatcher valueMatcher = matchingConfig.getValueMatcher();

            boolean matched = valueMatcher.match(value1, value2);

            if (matched) {
                matchScore += matchingConfig.getScore();
            } else {
                matchingResult.getUnmatchedColumns().add(matchingConfig.getColumnName());
            }

        }

        BigDecimal matchingPercentage = new BigDecimal(matchScore).divide(new BigDecimal(getTotalScore()));
        matchingResult.setMatchingPercentage(matchingPercentage);

        return matchingResult;
    }

    private Integer getTotalScore() {
        Integer totalScore = 0;
        for (MatchingConfig matchingConfig : this.passRule) {
            totalScore += matchingConfig.getScore();
        }
        return totalScore;
    }

    public List<MatchingConfig> getPassRule() {
        return passRule;
    }
}
