package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Configure how a column will be used to match records
 *
 * @param <T> Column data type
 */
@Setter
@Getter
@Builder
public class MatchingCriteria<T> {
    private final String columnName;
    /**
     * the weight of this column
     */
    private final Integer score;

    private final ValueMatcher<T> valueMatcher;

    public MatchingCriteria(String columnName, Integer score, ValueMatcher<T> valueMatcher) {
        if (columnName == null || score == null || score <= 0 || valueMatcher == null) {
            throw new IllegalArgumentException();
        }

        this.columnName = columnName;
        this.score = score;
        this.valueMatcher = valueMatcher;
    }
}
