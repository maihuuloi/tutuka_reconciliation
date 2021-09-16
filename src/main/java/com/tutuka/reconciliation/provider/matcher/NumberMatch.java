package com.tutuka.reconciliation.provider.matcher;

public class NumberMatch<T extends Number> implements ValueMatcher<T>{
    @Override
    public boolean match(T value1, T value2) {
        return value1.equals(value2);
    }
}
