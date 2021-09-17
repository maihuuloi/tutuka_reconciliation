package com.tutuka.txmanagement.reconciliation.matcher;

public class StringIgnoreCaseMatcher implements ValueMatcher<String>{
    @Override
    public boolean match(String value1, String value2) {
        return value1.equalsIgnoreCase(value2);
    }
}
