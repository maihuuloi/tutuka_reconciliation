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
     * @param record1 record to be compare
     * @param record2 record to be compare
     * @return matching result has been computed
     */
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
            }

        }

        BigDecimal matchingPercentage = new BigDecimal(matchScore).divide(new BigDecimal(getTotalScore()),2, RoundingMode.HALF_EVEN);
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

    public List<MatchingCriteria> getPassRule() {
        return passRule;
    }
}
