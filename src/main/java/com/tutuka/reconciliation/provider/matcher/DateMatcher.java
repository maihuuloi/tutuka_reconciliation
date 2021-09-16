package com.tutuka.reconciliation.provider.matcher;

import java.util.Date;

public class DateMatcher implements ValueMatcher<Date>{
    @Override
    public boolean match(Date value1, Date value2) {
        return value1.compareTo(value2) == 0;
    }
}
