package com.tutuka.txmanagement.reconciliation.matcher;

/**
 * Define matching rule for 2 value
 * @param <T> Class of record's column
 */
public interface ValueMatcher<T> {
    /**
     * Compute matching for 2 value
     * @return true if matched
     */
    boolean match(T value1, T value2);
}
