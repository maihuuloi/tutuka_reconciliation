package com.tutuka.txmanagement.provider.matcher;

public class StringSimilarMatcher implements ValueMatcher<String>{
    @Override
    public boolean match(String value1, String value2) {
        if(value1.contains(value2)) {
            return true;
        } else if (value2.contains(value1)) {
            return true;
        }
        return false;
    }
}
