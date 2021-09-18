package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.matcher.EqualMatcher;
import org.junit.Assert;
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
        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldObjectRecord record2 = new TestFieldObjectRecord();
        record2.setTransactionID("1");
        record2.setTransactionAmount(1d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertSame(100, percentage);

    }

    @Test
    void compare_whenPartialFieldMatch_ThenReturnBetweenOneHundredAndZeroMatchingPercentage() {
        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldObjectRecord record2 = new TestFieldObjectRecord();
        record2.setTransactionID("2");
        record2.setTransactionAmount(1d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertTrue(percentage > 0 && percentage < 100);
    }

    @Test
    void compare_whenNoneFieldMatch_ThenReturnZeroMatchingPercentage() {
        TestFieldObjectRecord record1 = new TestFieldObjectRecord();
        record1.setTransactionID("1");
        record1.setTransactionAmount(1d);
        TestFieldObjectRecord record2 = new TestFieldObjectRecord();
        record2.setTransactionID("2");
        record2.setTransactionAmount(2d);
        MatchingResult compare = recordMatcher.compare(record1, record2);
        Integer percentage = compare.getMatchingPercentage();
        Assert.assertSame(0, percentage);

    }


    @Test
    void compare_whenInputNull_ThenThrowException() {
        MatchingResult compare = recordMatcher.compare(null, null);

    }
}
