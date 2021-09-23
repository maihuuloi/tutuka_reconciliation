package com.tutuka.txmanagement.reconciliation.strategy;

import com.tutuka.txmanagement.reconciliation.MatchingResult;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.TestFieldRecord;
import com.tutuka.txmanagement.reconciliation.model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndexReconciliationStrategyTest {
    @Mock
    private GreedyReconciliationStrategy greedyReconciliationStrategy;
    @Mock
    RecordMatcher recordMatcher;

    private IndexReconciliationStrategy indexReconciliationStrategy;

    @BeforeEach
    public void setup() {
        indexReconciliationStrategy = new IndexReconciliationStrategy("TransactionID", recordMatcher);
        ReflectionTestUtils.setField(indexReconciliationStrategy, "greedyReconciliationStrategy", greedyReconciliationStrategy);
    }

    @Test
    void reconcile_WhenTwoRecordWithMatchIndex_ThenReturnResultWithPercentageFromRecordMatcher() {
        // Arrange

        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID("1");
        source1Records.add(record1);
        source2Records.add(record1);

        int expectedPercentage = 100;
        MatchingResult perfectMatch = MatchingResult.builder().matchingPercentage(expectedPercentage).build();
        when(recordMatcher.compare(record1, record1)).thenReturn(perfectMatch);

        // Act
        List<ReconciliationResult> actualReconcileResult = indexReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertFalse(actualReconcileResult.isEmpty());
        MatchingResult matchingResult = actualReconcileResult.get(0).getMatchingResult();
        assertSame(expectedPercentage, matchingResult.getMatchingPercentage());
    }

    @Test
    void reconcile_WhenIndexEmpty_ThenReturnResultFromGreedyStrategy() {
        // Arrange

        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID(null);
        source1Records.add(record1);
        source2Records.add(record1);

        List<ReconciliationResult> results = Arrays.asList(new ReconciliationResult(), new ReconciliationResult(), new ReconciliationResult());
        when(greedyReconciliationStrategy.reconcile(any(),any())).thenReturn(results);

        // Act
        List<ReconciliationResult> actualReconcileResult = indexReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertSame(results.size(),actualReconcileResult.size());
    }

    @Test
    void reconcile_WhenIndexNotMatch_ThenReturnResultFromGreedyStrategy() {
        // Arrange
        ArrayList<Record> source1Records = new ArrayList<Record>();
        ArrayList<Record> source2Records = new ArrayList<Record>();

        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionAmount(1d);
        record1.setTransactionID("1");
        source1Records.add(record1);

        TestFieldRecord record2 = new TestFieldRecord();
        record2.setTransactionAmount(0.0D);
        record2.setTransactionID("2");
        source2Records.add(record2);

        List<ReconciliationResult> results = Arrays.asList(new ReconciliationResult(), new ReconciliationResult(), new ReconciliationResult());

        when(greedyReconciliationStrategy.reconcile(any(),any())).thenReturn(results);

        // Act
        List<ReconciliationResult> actualReconcileResult = indexReconciliationStrategy.reconcile(source1Records,
                source2Records);

        // Assert
        assertSame(results.size(),actualReconcileResult.size());
    }


}

