package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        List<Record> file1NoKeyFoundRecords = new ArrayList<>();
        Map<Object, List<Record>> file2IdMap = source2Records.stream().collect(Collectors.groupingBy(r -> r.getValueByColumnName("TransactionID"), Collectors.toList()));
        for (Record file1Record : source1Records) {
            ReconciliationResult result = new ReconciliationResult();
            List<Record> file2RecordList = file2IdMap.get(file1Record.getValueByColumnName(indexColumn));
            if (file2RecordList == null || file2RecordList.isEmpty()) {
                file1NoKeyFoundRecords.add(file1Record);
                continue;
            } else {
                MatchingResult bestMatch = MatchingResult.zeroMatching();
                int index = 0;
                for (int i = 0; i < file2RecordList.size(); i++) {
                    MatchingResult matchingResult = recordMatcher.compare(file1Record, file2RecordList.get(i));
                    boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                    if (isHigherThanCurrentBestMatch) {
                        bestMatch = matchingResult;
                        index = i;
                    }
                }
                result.setMatchingResult(bestMatch);
                Record file2Record = file2RecordList.remove(index);
                source2Records.remove(file2Record);
                result.setRecord2(file2Record);
                result.setRecord1(file1Record);

                reconciliationResults.add(result);
            }
        }
        source1Records.clear();
        source1Records.addAll(file1NoKeyFoundRecords);

        List<ReconciliationResult> withoutIndex = greedyReconciliationStrategy.reconcile(source1Records, source2Records);
        reconciliationResults.addAll(withoutIndex);

        return reconciliationResults;
    }
}
