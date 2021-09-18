package com.tutuka.txmanagement.reconciliation.strategy;

import com.tutuka.txmanagement.reconciliation.MatchingResult;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.exception.InvalidDataException;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Reconcile record by using one column as key and perform comparison on the subset record which has index column identical
 */
public class IndexReconciliationStrategy implements ReconciliationStrategy {
    /**
     * Index column will help the search perform faster.
     * But there is a trade off, the result may not be the best match
     * since the matching process is performed on the subset of records which has index column identical.
     * <br/>When to use index column:
     * <ul>
     * <li>It perform best on column has unique value like ID, Transaction ID, Statement ID ...</li>
     * <li>Most of values in this column are potentially matched</li>
     * <li>Accepting that the matching result is not optimal sometimes</li>
     * </ul>
     */
    private final String indexColumn;

    private final RecordMatcher recordMatcher;

    private final GreedyReconciliationStrategy greedyReconciliationStrategy;

    public IndexReconciliationStrategy(String indexColumn, RecordMatcher recordMatcher) {
        this.indexColumn = indexColumn;
        this.recordMatcher = recordMatcher;
        this.greedyReconciliationStrategy = new GreedyReconciliationStrategy(recordMatcher);
    }

    @Override
    public List<ReconciliationResult> reconcile(List<Record> source1Records, List<Record> source2Records) {

        List<ReconciliationResult> reconciliationResults = new ArrayList<>();

        List<Record> source1NoKeyFoundRecords = new ArrayList<>();
        Map<Object, List<Record>> file2IdMap = index(source2Records);
        for (Record source1Record : source1Records) {
            ReconciliationResult result = new ReconciliationResult();
            Object indexValue = source1Record.getValueByColumnName(indexColumn);

            List<Record> source2RecordList = file2IdMap.get(indexValue);
            if (source2RecordList == null || source2RecordList.isEmpty()) {
                source1NoKeyFoundRecords.add(source1Record);
                continue;
            } else {
                MatchingResult bestMatch = MatchingResult.zeroMatching();
                int index = 0;
                for (int i = 0; i < source2RecordList.size(); i++) {
                    MatchingResult matchingResult = recordMatcher.compare(source1Record, source2RecordList.get(i));
                    boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                    if (isHigherThanCurrentBestMatch) {
                        bestMatch = matchingResult;
                        index = i;
                    }
                }
                result.setMatchingResult(bestMatch);
                Record source2Record = source2RecordList.remove(index);
                source2Records.remove(source2Record);
                result.setRecord2(source2Record);
                result.setRecord1(source1Record);

                reconciliationResults.add(result);
            }
        }
        source1Records.clear();
        source1Records.addAll(source1NoKeyFoundRecords);

        List<ReconciliationResult> withoutIndex = greedyReconciliationStrategy.reconcile(source1Records, source2Records);
        reconciliationResults.addAll(withoutIndex);

        return reconciliationResults;
    }

    private Map<Object, List<Record>> index(List<Record> source2Records) {
        Collector<Record, ?, Map<Object, List<Record>>> indexCollector = Collectors.groupingBy(r -> {

            Object value = r.getValueByColumnName(indexColumn);
            if (value == null) {
                throw new InvalidDataException("Index column can not be empty");
            }
            return value;
        }, Collectors.toList());
        Map<Object, List<Record>> file2IdMap = source2Records.stream().collect(indexCollector);
        return file2IdMap;
    }
}
