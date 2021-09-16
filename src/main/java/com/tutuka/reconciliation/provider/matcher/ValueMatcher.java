package com.tutuka.reconciliation.provider.matcher;

public interface ValueMatcher<T> {
    boolean match(T value1, T value2);
}
