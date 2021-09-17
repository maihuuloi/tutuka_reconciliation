package com.tutuka.reconciliation.provider.matcher;

import com.tutuka.reconciliation.provider.MatchingCriteria;
import com.tutuka.reconciliation.provider.MatchingResult;
import com.tutuka.reconciliation.provider.model.Record;

import java.math.BigDecimal;
import java.util.List;


public class OneToOneRecordMatcher implements RecordMatcher {
    List<MatchingCriteria> passRule;

    public OneToOneRecordMatcher(List<MatchingCriteria> passRule) {
        if (passRule == null || passRule.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.passRule = passRule;
    }

    @Override
    public MatchingResult compare(Record record1, Record record2) {

        MatchingResult matchingResult = new MatchingResult();
        Integer matchScore = 0;
        for (MatchingCriteria matchingCriteria : this.passRule) {
            Object value1 = record1.getValueByColumnName(matchingCriteria.getColumnName());
            Object value2 = record2.getValueByColumnName(matchingCriteria.getColumnName());
            ValueMatcher valueMatcher = matchingCriteria.getValueMatcher();
            boolean matched = valueMatcher.match(value1, value2);

            if (matched) {
                matchScore += matchingCriteria.getScore();
            } else {
                matchingResult.getUnmatchedColumns().add(matchingCriteria.getColumnName());

                if (matchingCriteria.isMustMatch()) {
                    matchScore = 0;
                    break;
                }
            }

        }

        BigDecimal matchingPercentage = new BigDecimal(matchScore).divide(new BigDecimal(getTotalScore()));
        matchingResult.setMatchingPercentage(matchingPercentage);

        return matchingResult;
    }

    private Integer getTotalScore() {
        Integer totalScore = 0;
        for (MatchingCriteria matchingCriteria : this.passRule) {
            totalScore += matchingCriteria.getScore();
        }
        return totalScore;
    }

}
