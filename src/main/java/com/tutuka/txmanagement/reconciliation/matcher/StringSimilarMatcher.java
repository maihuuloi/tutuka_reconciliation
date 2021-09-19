package com.tutuka.txmanagement.reconciliation.matcher;

import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;

import java.util.Locale;


public class StringSimilarMatcher implements ValueMatcher<String> {
    /**
     * Percentage of similarity to be consider as matched
     */
    private static final Double LOWEST_MATCHING_PERCENTAGE = 0.9;

    @Override
    public boolean compare(String value1, String value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException("Compare null value");
        }

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        Double distance = jaroWinklerDistance.apply(value1, value2);
        return distance > LOWEST_MATCHING_PERCENTAGE;
    }

}
