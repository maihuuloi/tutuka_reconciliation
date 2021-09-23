package com.tutuka.txmanagement.reconciliation.matcher;

import com.tutuka.txmanagement.reconciliation.constant.Constants;
import org.apache.commons.text.similarity.EditDistance;
import org.apache.commons.text.similarity.JaroWinklerDistance;


public class StringSimilarMatcher implements ValueMatcher<String> {

    @Override
    public boolean compare(String value1, String value2) {
        if (value1 == null || value2 == null) {
            throw new IllegalArgumentException("Compare null value");
        }

        EditDistance<Double> editDistance = new JaroWinklerDistance();
        Double distance = editDistance.apply(value1, value2);
        return distance > Constants.MATCHER_LOWEST_SIMILARITY_PERCENTAGE;
    }

}
