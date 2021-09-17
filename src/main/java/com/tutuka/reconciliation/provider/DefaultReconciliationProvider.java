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

public class DefaultReconciliationProvider extends ReconciliationProvider{


    public DefaultReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser) {
        super(recordMatcher, fileParser);
    }

    protected List<TransactionRecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records) {
        List<TransactionRecitationResult> recitationResults = new ArrayList<>();

        for (Record record1 : file1Records) {
            TransactionRecitationResult result = new TransactionRecitationResult();
            MatchingResult bestMatch = MatchingResult.zeroMatching();
            int index = 0;
            for (int i = 0; i < file2Records.size(); i++) {
                MatchingResult matchingResult = recordMatcher.compare(record1, file2Records.get(i));
                boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                if (isHigherThanCurrentBestMatch) {
                    bestMatch = matchingResult;
                    index = i;
                }
            }
            result.setMatchingResult(bestMatch);
            Record record2 = file2Records.remove(index);
            result.setRecord2(record2);

            result.setRecord1(record1);

            recitationResults.add(result);
        }

        return recitationResults;
    }
}
