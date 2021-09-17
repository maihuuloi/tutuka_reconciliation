package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingCriteria<T> {
    private String columnName;
    private Integer score;
    private ValueMatcher<T> valueMatcher;
    private boolean mustMatch;
}
