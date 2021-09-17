package com.tutuka.txmanagement.reconciliation.matcher;

public interface ValueMatcher<T> {
    boolean match(T value1, T value2);
}
