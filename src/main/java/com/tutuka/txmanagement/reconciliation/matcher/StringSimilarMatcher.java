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
    public boolean match(String value1, String value2) {
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        Double distance = jaroWinklerDistance.apply(value1, value2);
        return distance > LOWEST_MATCHING_PERCENTAGE;
    }

}
