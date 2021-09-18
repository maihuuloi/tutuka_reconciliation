package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configure how a column will be used to match records
 * @param <T> Column data type
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingCriteria<T> {
    private String columnName;
    /**
     * the weight of this column
     */
    private Integer score;
    private ValueMatcher<T> valueMatcher;
}
