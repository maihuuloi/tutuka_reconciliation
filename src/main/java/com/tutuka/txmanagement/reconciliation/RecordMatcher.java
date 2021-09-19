package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Matching records by using a pass rule which contain a set of matching criteria
 */
public class RecordMatcher {

    private List<MatchingCriteria> passRule;

    public RecordMatcher(List<MatchingCriteria> passRule) {
        if (passRule == null || passRule.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.passRule = passRule;
    }

    /**
     * Compute matching for 2 record by pass rule
     *
     * @param record1 record to be compare
     * @param record2 record to be compare
     * @return matching result has been computed
     */
    public MatchingResult compare(Record record1, Record record2) {
        if (record1 == null || record2 == null) {
            throw new IllegalArgumentException("Record must not be null");
        }
        MatchingResult matchingResult = new MatchingResult();
        Integer matchScore = 0;
        for (MatchingCriteria matchingCriteria : this.passRule) {
            Object value1 = record1.getValueByColumnName(matchingCriteria.getColumnName());
            Object value2 = record2.getValueByColumnName(matchingCriteria.getColumnName());

            if (value1 == null && value2 == null) {
                continue;//skip comparing
                //throw new InvalidDataException("Criteria column can not be empty");
            } else if(value1 == null || value2 == null) {
                matchingResult.getUnmatchedColumns().add(matchingCriteria.getColumnName());
                continue;
            }

            ValueMatcher valueMatcher = matchingCriteria.getValueMatcher();

            boolean matched = valueMatcher.compare(value1, value2);

            if (matched) {
                matchScore += matchingCriteria.getScore();
            } else {
                matchingResult.getUnmatchedColumns().add(matchingCriteria.getColumnName());
            }

        }

        BigDecimal matchingPercentage = new BigDecimal(matchScore).multiply(new BigDecimal(100))
                .divide(new BigDecimal(getTotalScore()), 2, RoundingMode.HALF_EVEN);
        matchingResult.setMatchingPercentage(matchingPercentage.intValue());

        return matchingResult;
    }


    private Integer getTotalScore() {
        Integer totalScore = 0;
        for (MatchingCriteria matchingCriteria : this.passRule) {
            totalScore += matchingCriteria.getScore();
        }
        return totalScore;
    }

    public List<MatchingCriteria> getPassRule() {
        return passRule;
    }
}
