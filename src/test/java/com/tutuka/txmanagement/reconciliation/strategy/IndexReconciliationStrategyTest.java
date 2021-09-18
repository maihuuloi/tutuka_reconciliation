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

class IndexReconciliationStrategyTest {
    @Test
    void reconcile() {
        // Arrange
        ArrayList<MatchingCriteria> matchingCriteriaList = new ArrayList<MatchingCriteria>();
        matchingCriteriaList.add(new MatchingCriteria("Column Name", 3, (ValueMatcher<Object>) mock(ValueMatcher.class)));
        IndexReconciliationStrategy indexReconciliationStrategy = new IndexReconciliationStrategy("Index Column",
                new RecordMatcher(matchingCriteriaList));
        ArrayList<Record> recordList = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        // Act
        List<ReconciliationResult> actualReconcileResult = indexReconciliationStrategy.reconcile(recordList,
                source2Records);

        // Assert
        assertTrue(actualReconcileResult.isEmpty());
        assertTrue(recordList.isEmpty());
    }
}

