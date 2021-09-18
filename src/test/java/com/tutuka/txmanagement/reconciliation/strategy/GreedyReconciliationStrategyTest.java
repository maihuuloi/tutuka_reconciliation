package com.tutuka.txmanagement.reconciliation.strategy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.tutuka.txmanagement.reconciliation.MatchingCriteria;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.ValueMatcher;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GreedyReconciliationStrategyTest {
    @Test
    void reconcile() {
        // Arrange
        ArrayList<MatchingCriteria> matchingCriteriaList = new ArrayList<MatchingCriteria>();
        MatchingCriteria column_name = new MatchingCriteria("Column Name", 3, (ValueMatcher<Object>) mock(ValueMatcher.class));
        matchingCriteriaList.add(column_name);
        GreedyReconciliationStrategy greedyReconciliationStrategy = new GreedyReconciliationStrategy(
                new RecordMatcher(matchingCriteriaList));
        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        // Act
        List<ReconciliationResult> actualReconcileResult = greedyReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertTrue(actualReconcileResult.isEmpty());
    }
}

