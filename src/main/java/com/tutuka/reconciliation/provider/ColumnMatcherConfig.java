package com.tutuka.reconciliation.provider;

import com.tutuka.reconciliation.provider.matcher.ValueMatcher;
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
public class ColumnMatcherConfig<T> {
    private String columnName;
    private Integer score;
    private ValueMatcher<T> valueMatcher;
    private boolean mustMatch;
}
