package com.tutuka.txmanagement.reconciliation;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.reconciliation.matcher.MatchingResult;
import com.tutuka.txmanagement.reconciliation.matcher.RecordMatcher;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public  class ReconciliationProvider {
    protected RecordMatcher recordMatcher;
    private FileParser fileParser;
    private String keyColumn;

    public ReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser) {
        this.recordMatcher = recordMatcher;
        this.fileParser = fileParser;
    }

    public ReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser, String keyColumn) {
        this.recordMatcher = recordMatcher;
        this.fileParser = fileParser;
        this.keyColumn = keyColumn;
    }


    public List<RecitationResult> reconcile(File source1, File source2) throws InvalidFileException {
        List<Record> source1Records = null;
        List<Record> source2Records = null;
        try {
            source1Records = fileParser.parse(source1);
            source2Records = fileParser.parse(source2);

        } catch (CsvException e) {
            throw new InvalidFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<RecitationResult> recitationResults = reconcile(source1Records, source2Records);

        return recitationResults;
    }


    protected List<RecitationResult> reconcile(List<Record> file1Records, List<Record> file2Records) {
        List<RecitationResult> recitationResults = new ArrayList<>();

        if (StringUtils.isNotEmpty(keyColumn)) {
            List<RecitationResult> withKey = reconcileWithKey(file1Records, file2Records);
            recitationResults.addAll(withKey);
            List<RecitationResult> withoutKey = reconcileWithoutKey(file1Records, file2Records);
            recitationResults.addAll(withoutKey);
        } else {
            List<RecitationResult> withoutKey = reconcileWithoutKey(file1Records, file2Records);
            recitationResults.addAll(withoutKey);
        }

        return recitationResults;
    }

    private List<RecitationResult> reconcileWithoutKey(List<Record> file1Records, List<Record> file2Records) {
        List<RecitationResult> recitationResults = new ArrayList<>();
        for (Record record1 : file1Records) {
            RecitationResult result = new RecitationResult();
            MatchingResult bestMatch = MatchingResult.zeroMatching();
            int index = -1;
            for (int i = 0; i < file2Records.size(); i++) {
                MatchingResult matchingResult = recordMatcher.compare(record1, file2Records.get(i));
                boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                if (isHigherThanCurrentBestMatch) {
                    bestMatch = matchingResult;
                    index = i;
                }
            }
            if (index == -1) {
                result.setMatchingResult(MatchingResult.zeroMatching());
            } else {
                result.setMatchingResult(bestMatch);
                Record record2 = file2Records.remove(index);
                result.setRecord2(record2);
            }

            result.setRecord1(record1);

            recitationResults.add(result);
        }


        List<RecitationResult> file2UnmatchedResult = file2Records.stream().map(t -> RecitationResult.builder().record2(t).matchingResult(MatchingResult.zeroMatching()).build())
                .collect(Collectors.toList());
        recitationResults.addAll(file2UnmatchedResult);
        return recitationResults;
    }

    private List<RecitationResult> reconcileWithKey(List<Record> file1Records, List<Record> file2Records) {
        List<RecitationResult> recitationResults = new ArrayList<>();

        List<Record> file1NoKeyFoundRecords = new ArrayList<>();
        Map<Object, List<Record>> file2IdMap = file2Records.stream().collect(Collectors.groupingBy(r -> r.getValueByColumnName("TransactionID"), Collectors.toList()));
        for (Record file1Record : file1Records) {
            RecitationResult result = new RecitationResult();
            List<Record> file2RecordList = file2IdMap.get(file1Record.getValueByColumnName(keyColumn));
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
                file2Records.remove(file2Record);
                result.setRecord2(file2Record);
                result.setRecord1(file1Record);

                recitationResults.add(result);
            }
        }
        file1Records.clear();
        file1Records.addAll(file1NoKeyFoundRecords);


        return recitationResults;
    }
}
