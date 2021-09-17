package com.tutuka.txmanagement.provider;

import com.tutuka.txmanagement.provider.matcher.ValueMatcher;
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
