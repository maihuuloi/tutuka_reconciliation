package com.tutuka.txmanagement.reconciliation.strategy;

import com.tutuka.txmanagement.reconciliation.MatchingResult;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.constant.Constants;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reconcile records by comparing one by one to find best match records
 */
public class GreedyReconciliationStrategy implements ReconciliationStrategy{
    private final RecordMatcher recordMatcher;

    public GreedyReconciliationStrategy(RecordMatcher recordMatcher) {
        this.recordMatcher = recordMatcher;
    }

    @Override
    public List<ReconciliationResult> reconcile(List<Record> source1Records, List<Record> source2Records) {
        List<ReconciliationResult> reconciliationResults = new ArrayList<>();
        for (Record record1 : source1Records) {
            ReconciliationResult result = new ReconciliationResult();
            MatchingResult bestMatch = MatchingResult.zeroMatching();
            int index = -1;
            for (int i = 0; i < source2Records.size(); i++) {
                MatchingResult matchingResult = recordMatcher.compare(record1, source2Records.get(i));
                boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                if (isHigherThanCurrentBestMatch) {
                    bestMatch = matchingResult;
                    index = i;
                }

                boolean isPerfectMatch = bestMatch.getMatchingPercentage() == Constants.ONE_HUNDRED_PERCENTAGE;
                if (isPerfectMatch) {
                    break;
                }
            }
            if (index == -1) {
                result.setMatchingResult(MatchingResult.zeroMatching());
            } else {
                result.setMatchingResult(bestMatch);
                Record record2 = source2Records.remove(index);
                result.setRecord2(record2);
            }

            result.setRecord1(record1);

            reconciliationResults.add(result);
        }


        List<ReconciliationResult> file2UnmatchedResult = source2Records.stream().map(t -> ReconciliationResult.builder().record2(t).matchingResult(MatchingResult.zeroMatching()).build())
                .collect(Collectors.toList());
        reconciliationResults.addAll(file2UnmatchedResult);
        return reconciliationResults;
    }
}
