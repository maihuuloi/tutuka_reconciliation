package com.tutuka.txmanagement.provider.matcher;

public interface ValueMatcher<T> {
    boolean match(T value1, T value2);
}
