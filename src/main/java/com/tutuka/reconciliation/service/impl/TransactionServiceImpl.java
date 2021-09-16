package com.tutuka.reconciliation.service.impl;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;
import com.tutuka.reconciliation.dto.TransactionRecitationResult;
import com.tutuka.reconciliation.dto.TransactionRecord;
import com.tutuka.reconciliation.exception.BadRequestException;
import com.tutuka.reconciliation.processor.CsvParser;
import com.tutuka.reconciliation.provider.MatchingResult;
import com.tutuka.reconciliation.provider.ReconciliationProvider;
import com.tutuka.reconciliation.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ReconciliationProvider reconciliationProvider;
    @Autowired
    private CsvParser csvParser;

    @Override
    public ReconciliationOverviewResponse getConciliationOverview(File file1, File file2) {
        List<TransactionRecitationResult> recitationResults = getTransactionRecitationResults(file1, file2);

        Integer file1TotalCount = 0;
        Integer file1MatchingCount = 0;
        Integer file1UnmatchedCount = 0;
        Integer file2TotalCount = 0;
        Integer file2MatchingCount = 0;
        Integer file2UnmatchedCount = 0;
        for (TransactionRecitationResult recitationResult : recitationResults) {
            if (recitationResult.getRecord1() == null) {
                file2TotalCount++;
                file2UnmatchedCount++;
            } else if (recitationResult.getRecord2() == null) {
                file1TotalCount++;
                file1UnmatchedCount++;
            } else {//both record 1 and 2 present => transaction ids are matching
                boolean perfectMatch = recitationResult.getMatchingPercentage().equals(BigDecimal.ONE);
                if (perfectMatch) {
                    file1MatchingCount++;
                    file1TotalCount++;
                    file2MatchingCount++;
                    file2TotalCount++;
                }
            }
        }

        ReconciliationOverviewResponse response = new ReconciliationOverviewResponse();
        response.setFile1TotalCount(file1TotalCount);
        response.setFile1MatchingCount(file1MatchingCount);
        response.setFile1UnmatchedCount(file1UnmatchedCount);
        response.setFile2TotalCount(file2TotalCount);
        response.setFile2MatchingCount(file2MatchingCount);
        response.setFile2UnmatchedCount(file2UnmatchedCount);

        return response;
    }

    private List<TransactionRecitationResult> getTransactionRecitationResults(File file1, File file2) {
        List<TransactionRecord> file1TransactionRecords = null;
        List<TransactionRecord> file2TransactionRecords = null;
        try {
            file1TransactionRecords = csvParser.parse(file1);
            file2TransactionRecords = csvParser.parse(file2);

        } catch (CsvException e) {
            throw new BadRequestException("transaction.invalid-format-file");
        } catch (IOException e) {
            //TODO: internal exception
//            throw e;
        }

        Map<String, List<TransactionRecord>> file2IdMap = file2TransactionRecords.stream().collect(Collectors.groupingBy(TransactionRecord::getTransactionID, Collectors.toList()));

        List<TransactionRecitationResult> recitationResults = new ArrayList<>();

        for (TransactionRecord transactionRecord1 : file1TransactionRecords) {
            TransactionRecitationResult result = new TransactionRecitationResult();
            List<TransactionRecord> transactionRecord2List = file2IdMap.get(transactionRecord1.getTransactionID());
            if (transactionRecord2List == null || transactionRecord2List.isEmpty()) {
                result.setMatchingResult(MatchingResult.zeroMatching());
            } else {
                MatchingResult bestMatch = MatchingResult.zeroMatching();
                int index = 0;
                for (int i = 0; i < transactionRecord2List.size(); i++) {
                    MatchingResult matchingResult = reconciliationProvider.calculate(transactionRecord1, transactionRecord2List.get(i));
                    boolean isHigherThanCurrentBestMatch = bestMatch.getMatchingPercentage().compareTo(matchingResult.getMatchingPercentage()) == -1;

                    if (isHigherThanCurrentBestMatch) {
                        bestMatch = matchingResult;
                        index = i;
                    }
                }
                result.setMatchingResult(bestMatch);
                TransactionRecord transactionRecord2 = transactionRecord2List.remove(index);
                result.setRecord2(transactionRecord2);
            }

            result.setRecord1(transactionRecord1);

            recitationResults.add(result);
        }

        List<TransactionRecitationResult> file2UnmatchedResult = file2IdMap.values().stream().
                flatMap(s -> s.stream()).map(t -> TransactionRecitationResult.builder().record2(t).matchingResult(MatchingResult.zeroMatching()).build())
                .collect(Collectors.toList());
        recitationResults.addAll(file2UnmatchedResult);

        return recitationResults;
    }

}
