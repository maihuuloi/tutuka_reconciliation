package com.tutuka.txmanagement.service.impl;

import com.tutuka.txmanagement.dto.ReconciliationOverviewResponse;
import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
import com.tutuka.txmanagement.provider.RecitationResult;
import com.tutuka.txmanagement.exception.BadRequestException;
import com.tutuka.txmanagement.provider.ReconciliationProvider;
import com.tutuka.txmanagement.provider.exception.InvalidFileException;
import com.tutuka.txmanagement.service.TransactionService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final ReconciliationProvider reconciliationProvider;

    public TransactionServiceImpl(ReconciliationProvider reconciliationProvider) {
        this.reconciliationProvider = reconciliationProvider;
    }

    public ReconciliationResultResponse reconcile(File file1, File file2) {
        ReconciliationResultResponse response = new ReconciliationResultResponse();
        List<RecitationResult> recitationResults;
        try {
            recitationResults = reconciliationProvider.reconcile(file1, file2);
        } catch (InvalidFileException e) {
            throw new BadRequestException("transaction.invalid-format-file");
        }

        ReconciliationOverviewResponse reconciliationOverviewResponse = toConciliationOverviewResponse(recitationResults);
        response.setReconciliationOverview(reconciliationOverviewResponse);

        List<RecitationResult> unmatchedRecords = getUnmatchedRecords(recitationResults);
        response.setUnmatchedRecords(unmatchedRecords);

        List<RecitationResult> matchingRecords = getMatchingRecords(recitationResults);
        response.setMatchingRecords(matchingRecords);

        List<RecitationResult> suggestedRecords = getSuggestedRecords(recitationResults);
        response.setSuggestedRecords(suggestedRecords);

        return response;

    }

    private List<RecitationResult> getUnmatchedRecords(List<RecitationResult> recitationResults) {
        return recitationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ZERO)==0)
                .collect(Collectors.toList());
    }

    private List<RecitationResult> getMatchingRecords(List<RecitationResult> recitationResults) {
        return recitationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ONE)==0)
                .collect(Collectors.toList());
    }

    private List<RecitationResult> getSuggestedRecords(List<RecitationResult> recitationResults) {
        return recitationResults
                .stream().filter(r -> r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ONE) == -1
                        && r.getMatchingResult().getMatchingPercentage().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.toList());
    }

    public ReconciliationOverviewResponse toConciliationOverviewResponse(List<RecitationResult> recitationResults) {
        Integer file1TotalCount = 0;
        Integer file1MatchingCount = 0;
        Integer file1UnmatchedCount = 0;
        Integer file2TotalCount = 0;
        Integer file2MatchingCount = 0;
        Integer file2UnmatchedCount = 0;
        for (RecitationResult recitationResult : recitationResults) {
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
