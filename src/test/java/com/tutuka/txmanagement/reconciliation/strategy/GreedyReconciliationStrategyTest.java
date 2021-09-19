package com.tutuka.txmanagement.reconciliation.strategy;

import com.tutuka.txmanagement.reconciliation.MatchingResult;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.TestFieldObjectRecord;
import com.tutuka.txmanagement.reconciliation.model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreedyReconciliationStrategyTest {
    private GreedyReconciliationStrategy greedyReconciliationStrategy;
    @Mock
    RecordMatcher recordMatcher;

    @BeforeEach
    public void setup() {
        greedyReconciliationStrategy = new GreedyReconciliationStrategy(recordMatcher);
    }

    @Test
    void reconcile_WhenTwoRecordValid_ThenReturnResultWithPercentageFromRecordMatcher() {
        // Arrange
        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID("1");
        source1Records.add(record1);
        source2Records.add(record1);
        int expectedPercentage = 100;
        MatchingResult perfectMatch = MatchingResult.builder().matchingPercentage(expectedPercentage).build();
        when(recordMatcher.compare(record1, record1)).thenReturn(perfectMatch);

        // Act
        List<ReconciliationResult> actualReconcileResult = greedyReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertFalse(actualReconcileResult.isEmpty());
        MatchingResult matchingResult = actualReconcileResult.get(0).getMatchingResult();
        assertSame(expectedPercentage, matchingResult.getMatchingPercentage());
    }

    @Test
    void reconcile_WhenValidRecordAndRecordMatcherReturnGreaterThanZero_ThenReturnMatchingResult() {
        // Arrange
        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID("1");
        source1Records.add(record1);
        source2Records.add(record1);
        MatchingResult perfectMatch = MatchingResult.builder().matchingPercentage(100).build();
        when(recordMatcher.compare(record1, record1)).thenReturn(perfectMatch);

        // Act
        List<ReconciliationResult> actualReconcileResult = greedyReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertSame(1,actualReconcileResult.size());
        assertNotNull(actualReconcileResult.get(0).getRecord1());
        assertNotNull(actualReconcileResult.get(0).getRecord2());
    }

    @Test
    void reconcile_WhenValidRecordAndRecordMatcherReturnZero_ThenReturnUnmatchedResults() {
        // Arrange
        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID("1");
        source1Records.add(record1);
        source2Records.add(record1);
        MatchingResult perfectMatch = MatchingResult.builder().matchingPercentage(0).build();
        when(recordMatcher.compare(record1, record1)).thenReturn(perfectMatch);

        // Act
        List<ReconciliationResult> actualReconcileResult = greedyReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertSame(2,actualReconcileResult.size());
    }
}

