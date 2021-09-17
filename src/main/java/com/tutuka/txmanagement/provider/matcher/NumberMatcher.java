package com.tutuka.txmanagement.provider.matcher;

public class NumberMatcher<T extends Number> implements ValueMatcher<T>{
    @Override
    public boolean match(T value1, T value2) {
        return value1.equals(value2);
    }
}
