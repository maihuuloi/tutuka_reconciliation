package com.tutuka.reconciliation.provider;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.dto.TransactionRecitationResult;
import com.tutuka.reconciliation.provider.exception.InvalidFileException;
import com.tutuka.reconciliation.provider.matcher.RecordMatcher;
import com.tutuka.reconciliation.provider.model.Record;
import com.tutuka.reconciliation.provider.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyColumnReconciliationProvider extends ReconciliationProvider {

    private String keyColumn;

    public KeyColumnReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser, String keyColumn) {
       super(recordMatcher,fileParser);
        this.keyColumn = keyColumn;
    }



    protected List<TransactionRecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records) {
        List<TransactionRecitationResult> recitationResults = new ArrayList<>();

        Map<Object, List<Record>> file2IdMap = file2Records.stream().collect(Collectors.groupingBy(r -> r.getValueByColumnName("TransactionID"), Collectors.toList()));
        for (Record record1 : file1Records) {
            TransactionRecitationResult result = new TransactionRecitationResult();
            List<Record> record2List = file2IdMap.get(record1.getValueByColumnName(keyColumn));
            if (record2List == null || record2List.isEmpty()) {
                result.setMatchingResult(MatchingResult.zeroMatching());
            } else {
                MatchingResult bestMatch = MatchingResult.zeroMatching();
                int index = 0;
                for (int i = 0; i < record2List.size(); i++) {
                    MatchingResult matchingResult = recordMatcher.compare(record1, record2List.get(i));
                    boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                    if (isHigherThanCurrentBestMatch) {
                        bestMatch = matchingResult;
                        index = i;
                    }
                }
                result.setMatchingResult(bestMatch);
                Record record2 = record2List.remove(index);
                result.setRecord2(record2);
            }

            result.setRecord1(record1);

            recitationResults.add(result);
        }

        List<TransactionRecitationResult> file2UnmatchedResult = file2IdMap.values().stream().
                flatMap(s -> s.stream()).map(t -> TransactionRecitationResult.builder().record2(t).matchingResult(MatchingResult.zeroMatching()).build())
                .collect(Collectors.toList());
        recitationResults.addAll(file2UnmatchedResult);
        return recitationResults;
    }
}
