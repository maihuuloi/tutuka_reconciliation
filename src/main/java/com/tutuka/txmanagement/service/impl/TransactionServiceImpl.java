package com.tutuka.txmanagement.service.impl;

import com.tutuka.txmanagement.dto.ReconciliationOverviewResponse;
import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
import com.tutuka.txmanagement.exception.BadRequestException;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.ReconciliationProvider;
import com.tutuka.txmanagement.reconciliation.exception.ColumnNameNotFoundException;
import com.tutuka.txmanagement.reconciliation.exception.InvalidFileException;
import com.tutuka.txmanagement.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final ReconciliationProvider reconciliationProvider;

    public TransactionServiceImpl(ReconciliationProvider reconciliationProvider) {
        this.reconciliationProvider = reconciliationProvider;
    }

    public ReconciliationResultResponse reconcile(File file1, File file2) {
        ReconciliationResultResponse response = new ReconciliationResultResponse();
        List<ReconciliationResult> reconciliationResults;
        try {
            reconciliationResults = reconciliationProvider.reconcile(file1, file2);
        } catch (InvalidFileException | ColumnNameNotFoundException e) {
            throw new BadRequestException("transaction.reconciliation.invalid-format-file", "File format invalid", e);
        }

        ReconciliationOverviewResponse reconciliationOverviewResponse = toConciliationOverviewResponse(reconciliationResults);
        response.setReconciliationOverview(reconciliationOverviewResponse);

        List<ReconciliationResult> unmatchedRecords = getUnmatchedRecords(reconciliationResults);
        response.setUnmatchedRecords(unmatchedRecords);

        List<ReconciliationResult> matchingRecords = getMatchingRecords(reconciliationResults);
        response.setMatchingRecords(matchingRecords);

        List<ReconciliationResult> suggestedRecords = getSuggestedRecords(reconciliationResults);
        response.setSuggestedRecords(suggestedRecords);

        return response;

    }

    private List<ReconciliationResult> getUnmatchedRecords(List<ReconciliationResult> reconciliationResults) {
        return reconciliationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ZERO) == 0)
                .collect(Collectors.toList());
    }

    private List<ReconciliationResult> getMatchingRecords(List<ReconciliationResult> reconciliationResults) {
        return reconciliationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ONE) == 0)
                .collect(Collectors.toList());
    }

    private List<ReconciliationResult> getSuggestedRecords(List<ReconciliationResult> reconciliationResults) {
        return reconciliationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ONE) == -1
                        && r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.toList());
    }

    public ReconciliationOverviewResponse toConciliationOverviewResponse(List<ReconciliationResult> reconciliationResults) {
        Integer file1TotalCount = 0;
        Integer file1MatchingCount = 0;
        Integer file1UnmatchedCount = 0;
        Integer file2TotalCount = 0;
        Integer file2MatchingCount = 0;
        Integer file2UnmatchedCount = 0;
        for (ReconciliationResult reconciliationResult : reconciliationResults) {
            if (reconciliationResult.getRecord1() == null) {
                file2TotalCount++;
                file2UnmatchedCount++;
            } else if (reconciliationResult.getRecord2() == null) {
                file1TotalCount++;
                file1UnmatchedCount++;
            } else {//both record 1 and 2 present => transaction ids are matching
                boolean perfectMatch = reconciliationResult.getMatchingPercentage().equals(BigDecimal.ONE);
                if (perfectMatch) {
                    file1MatchingCount++;
                    file1TotalCount++;
                    file2MatchingCount++;
                    file2TotalCount++;
                } else {
                    file1UnmatchedCount++;
                    file1TotalCount++;
                    file2UnmatchedCount++;
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


}
