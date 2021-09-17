package com.tutuka.txmanagement.reconciliation;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.reconciliation.matcher.MatchingResult;
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
    protected final RecordMatcher recordMatcher;
    private final List<MatchingConfig> passRule;
    private final FileParser fileParser;

    public ReconciliationProvider(List<MatchingConfig> passRule, FileParser fileParser) {
        this.passRule = passRule;
        this.fileParser = fileParser;
        this.recordMatcher = new RecordMatcher(passRule);
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
        String indexColumn = getIndexColumn();
        if (StringUtils.isNotEmpty(indexColumn)) {
            List<RecitationResult> withIndex = reconcileWithIndex(file1Records, file2Records, indexColumn);
            recitationResults.addAll(withIndex);
            List<RecitationResult> withoutIndex = reconcileWithoutIndex(file1Records, file2Records);
            recitationResults.addAll(withoutIndex);
        } else {
            List<RecitationResult> withoutIndex = reconcileWithoutIndex(file1Records, file2Records);
            recitationResults.addAll(withoutIndex);
        }

        return recitationResults;
    }

    private String getIndexColumn() {

        for (MatchingConfig matchingConfig : passRule) {
            if (matchingConfig.isIndex()) {
                return matchingConfig.getColumnName();
            }
        }
        return null;
    }

    private List<RecitationResult> reconcileWithoutIndex(List<Record> file1Records, List<Record> file2Records) {
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

    private List<RecitationResult> reconcileWithIndex(List<Record> file1Records, List<Record> file2Records, String indexColumn) {
        List<RecitationResult> recitationResults = new ArrayList<>();

        List<Record> file1NoKeyFoundRecords = new ArrayList<>();
        Map<Object, List<Record>> file2IdMap = file2Records.stream().collect(Collectors.groupingBy(r -> r.getValueByColumnName("TransactionID"), Collectors.toList()));
        for (Record file1Record : file1Records) {
            RecitationResult result = new RecitationResult();
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
