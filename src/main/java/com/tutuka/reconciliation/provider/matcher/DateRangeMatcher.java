package com.tutuka.reconciliation.provider.matcher;

import java.util.Date;

/**
 * Check if 2 date value is different in a range
 */
public class DateRangeMatcher implements ValueMatcher<Date> {
    private static Integer RANGE = 3;

    @Override
    public boolean match(Date value1, Date value2) {
        Long oneDay = 1000 * 60 * 60 * 24l;
        long dayDifferent = (value1.getTime() - value2.getTime()) / oneDay;
        return dayDifferent <= RANGE;
    }
}
