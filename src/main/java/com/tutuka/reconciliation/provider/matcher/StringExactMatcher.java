package com.tutuka.reconciliation.provider.matcher;

public class StringExactMatcher implements ValueMatcher<String>{
    @Override
    public boolean match(String value1, String value2) {
        return value1.equals(value2);
    }
}
