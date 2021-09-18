package com.tutuka.txmanagement.reconciliation;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reconcile 2 set of data sources
 * @implNote This is better to become an interface, but YAGNI.
 */
public class ReconciliationProvider {
    protected final RecordMatcher recordMatcher;

    /**
     * Index column will help the search perform faster.
     * But there is a trade off, the result may not be the best match
     * since the matching process is performed on the subset of records which has index column equal.
     * <br/>When to use index column:
     * <ul>
     * <li>It perform best on column has unique value like ID, Transaction ID, Statement ID ...</li>
     * <li>Most of values in this column are potentially matched</li>
     * <li>Accepting that the matching result is not optimal sometimes</li>
     * </ul>
     */
    private final String indexColumn;

    private final FileParser fileParser;

    public ReconciliationProvider(RecordMatcher recordMatcher, FileParser fileParser, String indexColumn) {
        this.recordMatcher = recordMatcher;
        this.fileParser = fileParser;
        this.indexColumn = indexColumn;
    }

    /**
     * Parse two file and reconcile each line from one source to each line in the other source
     * to find the highest matching
     * @param source1 source input for reconcile
     * @param source2 source input for reconcile
     * @return A list of matching record result
     * @throws InvalidFileException when the provided data sources have invalid format content
     */
    public List<ReconciliationResult> reconcile(File source1, File source2) throws InvalidFileException {
        List<Record> source1Records;
        List<Record> source2Records;
        try {
            source1Records = fileParser.parse(source1);
            source2Records = fileParser.parse(source2);
        } catch (CsvException e) {
            throw new InvalidFileException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ReconciliationResult> reconciliationResults = reconcile(source1Records, source2Records);

        return reconciliationResults;
    }


    protected List<ReconciliationResult> reconcile(List<Record> file1Records, List<Record> file2Records) {
        List<ReconciliationResult> reconciliationResults = new ArrayList<>();
        String indexColumn = getIndexColumn();
        if (StringUtils.isNotEmpty(indexColumn)) {
            List<ReconciliationResult> withIndex = reconcileWithIndex(file1Records, file2Records, indexColumn);
            reconciliationResults.addAll(withIndex);
        } else {
            List<ReconciliationResult> withoutIndex = reconcileWithoutIndex(file1Records, file2Records);
            reconciliationResults.addAll(withoutIndex);
        }

        return reconciliationResults;
    }

    private String getIndexColumn() {
        return indexColumn;
    }

    private List<ReconciliationResult> reconcileWithoutIndex(List<Record> file1Records, List<Record> file2Records) {
        List<ReconciliationResult> reconciliationResults = new ArrayList<>();
        for (Record record1 : file1Records) {
            ReconciliationResult result = new ReconciliationResult();
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

            reconciliationResults.add(result);
        }


        List<ReconciliationResult> file2UnmatchedResult = file2Records.stream().map(t -> ReconciliationResult.builder().record2(t).matchingResult(MatchingResult.zeroMatching()).build())
                .collect(Collectors.toList());
        reconciliationResults.addAll(file2UnmatchedResult);
        return reconciliationResults;
    }

    private List<ReconciliationResult> reconcileWithIndex(List<Record> file1Records, List<Record> file2Records, String indexColumn) {
        List<ReconciliationResult> reconciliationResults = new ArrayList<>();

        List<Record> file1NoKeyFoundRecords = new ArrayList<>();
        Map<Object, List<Record>> file2IdMap = file2Records.stream().collect(Collectors.groupingBy(r -> r.getValueByColumnName("TransactionID"), Collectors.toList()));
        for (Record file1Record : file1Records) {
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
                file2Records.remove(file2Record);
                result.setRecord2(file2Record);
                result.setRecord1(file1Record);

                reconciliationResults.add(result);
            }
        }
        file1Records.clear();
        file1Records.addAll(file1NoKeyFoundRecords);

        List<ReconciliationResult> withoutIndex = reconcileWithoutIndex(file1Records, file2Records);
        reconciliationResults.addAll(withoutIndex);

        return reconciliationResults;
    }

    public static ReconciliationProviderBuilder builder() {
        return new ReconciliationProviderBuilder();
    }
}
