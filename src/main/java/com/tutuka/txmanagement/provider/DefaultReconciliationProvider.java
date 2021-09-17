package com.tutuka.txmanagement.provider;

import com.tutuka.txmanagement.provider.matcher.MatchingResult;
import com.tutuka.txmanagement.provider.matcher.RecordMatcher;
import com.tutuka.txmanagement.provider.model.Record;
import com.tutuka.txmanagement.provider.parser.FileParser;

import java.util.ArrayList;
import java.util.List;

public class DefaultReconciliationProvider extends ReconciliationProvider{


    public DefaultReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser) {
        super(recordMatcher, fileParser);
    }

    protected List<RecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records) {
        List<RecitationResult> recitationResults = new ArrayList<>();

        for (Record record1 : file1Records) {
            RecitationResult result = new RecitationResult();
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
