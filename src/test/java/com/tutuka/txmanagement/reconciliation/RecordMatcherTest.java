package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.EqualMatcher;
import com.tutuka.txmanagement.reconciliation.strategy.RecordMatcher;
import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RecordMatcherTest {

    private RecordMatcher recordMatcher;

    private List<MatchingCriteria> getPassRule() {
        List<MatchingCriteria> matchingCriteria = new ArrayList<>();
        MatchingCriteria transactionID = MatchingCriteria.<String>builder()
                .columnName("TransactionID")
                .score(4)
                .valueMatcher(new EqualMatcher<>())
                .build();
        matchingCriteria.add(transactionID);

        MatchingCriteria transactionAmount = MatchingCriteria.<Double>builder()
                .columnName("TransactionAmount")
                .score(1)
                .valueMatcher(new EqualMatcher<>())
                .build();

        matchingCriteria.add(transactionAmount);

        return matchingCriteria;
    }

    @BeforeEach
    void setUp() {
        recordMatcher = new RecordMatcher(getPassRule());
    }

    @Test
    void compare_whenAllFieldMatch_ThenReturnOneHundredMatchingPercentage() {
        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldRecord record2 = new TestFieldRecord();
        record2.setTransactionID("1");
        record2.setTransactionAmount(1d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertSame(100, percentage);

    }

    @Test
    void compare_whenPartialFieldMatch_ThenReturnBetweenOneHundredAndZeroMatchingPercentage() {
        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldRecord record2 = new TestFieldRecord();
        record2.setTransactionID("2");
        record2.setTransactionAmount(1d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertTrue(percentage > 0 && percentage < 100);
    }

    @Test
    void compare_whenNoFieldMatch_ThenReturnZeroMatchingPercentage() {
        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldRecord record2 = new TestFieldRecord();
        record2.setTransactionID("2");
        record2.setTransactionAmount(2d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertSame(0, percentage);

    }

    @Test
    void compare_whenRecordNull_ThenThrowException() {

        ThrowingRunnable throwingRunnable = () -> recordMatcher.compare(null, null);
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, throwingRunnable);

        Assert.assertTrue(exception.getMessage().contains("Record must not be null"));
    }

    @Test
    void compare_whenValueOfAFieldOnOneRecordNull_ThenConsiderThatFieldNotMatched() {
        TestFieldRecord record1 = new TestFieldRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldRecord record2 = new TestFieldRecord();
        record2.setTransactionID("1");
        record2.setTransactionAmount(null);
        MatchingResult result = recordMatcher.compare(record1, record2);
        Integer percentage = result.getMatchingPercentage();
        Assert.assertTrue(percentage < 100);
        Assert.assertTrue(result.getUnmatchedColumns().contains("TransactionAmount"));
    }
}
