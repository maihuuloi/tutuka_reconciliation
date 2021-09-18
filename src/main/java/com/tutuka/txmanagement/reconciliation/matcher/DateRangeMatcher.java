package com.tutuka.txmanagement.reconciliation.matcher;

import java.util.Date;

/**
 * Check if 2 date value is different in a range
 */
public class DateRangeMatcher implements ValueMatcher<Date> {
    private static Integer ACCEPT_RANGE_DAYS = 3;

    @Override
    public boolean match(Date value1, Date value2) {
        Float oneDay = 1000 * 60 * 60 * 24f;
        float dayDifferent = (value1.getTime() - value2.getTime()) / oneDay;
        return dayDifferent <= ACCEPT_RANGE_DAYS;
    }

}
