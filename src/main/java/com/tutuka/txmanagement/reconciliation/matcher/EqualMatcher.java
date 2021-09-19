package com.tutuka.txmanagement.reconciliation.matcher;

public class EqualMatcher<T> implements ValueMatcher<T>{
    @Override
    public boolean compare(T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException("Compare null value");
        }

        return value1.equals(value2);
    }
}
