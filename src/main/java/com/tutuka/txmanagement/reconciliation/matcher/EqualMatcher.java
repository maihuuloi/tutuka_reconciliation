package com.tutuka.txmanagement.reconciliation.matcher;

public class EqualMatcher<T> implements ValueMatcher<T>{
    @Override
    public boolean match(T value1, T value2) {
        return value1.equals(value2);
    }
}
