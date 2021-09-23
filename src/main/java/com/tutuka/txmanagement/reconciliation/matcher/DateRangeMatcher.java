package com.tutuka.txmanagement.reconciliation.matcher;

import com.tutuka.txmanagement.reconciliation.constant.Constants;

import java.util.Date;

/**
 * Check if 2 date value is different in a range
 */
public class DateRangeMatcher implements ValueMatcher<Date> {

    @Override
    public boolean compare(Date value1, Date value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException("Compare null value");
        }

        Float oneDay = 1000 * 60 * 60 * 24f;
        float dayDifferent = (value1.getTime() - value2.getTime()) / oneDay;
        return dayDifferent <= Constants.MATCHER_ACCEPT_RANGE_DAYS;
    }

}
