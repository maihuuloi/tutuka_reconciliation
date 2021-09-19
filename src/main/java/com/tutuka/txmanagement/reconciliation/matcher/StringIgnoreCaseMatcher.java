package com.tutuka.txmanagement.reconciliation.matcher;

public class StringIgnoreCaseMatcher implements ValueMatcher<String>{
    @Override
    public boolean compare(String value1, String value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException("Compare null value");
        }

        return value1.equalsIgnoreCase(value2);
    }
}
