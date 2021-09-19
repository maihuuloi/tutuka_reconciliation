package com.tutuka.txmanagement.service.impl;

import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
import com.tutuka.txmanagement.dto.ReconciliationSummaryResponse;
import com.tutuka.txmanagement.exception.BadRequestException;
import com.tutuka.txmanagement.reconciliation.ReconciliationProvider;
import com.tutuka.txmanagement.reconciliation.ReconciliationResult;
import com.tutuka.txmanagement.reconciliation.constant.Constants;
import com.tutuka.txmanagement.reconciliation.exception.ReconciliationException;
import com.tutuka.txmanagement.service.ReconciliationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService {
    private final ReconciliationProvider reconciliationProvider;

    public ReconciliationServiceImpl(ReconciliationProvider reconciliationProvider) {
        this.reconciliationProvider = reconciliationProvider;
    }

    public ReconciliationResultResponse reconcile(File file1, File file2) {
        ReconciliationResultResponse response = new ReconciliationResultResponse();

        log.debug("Start reconciliation process");
        List<ReconciliationResult> reconciliationResults;
        try {
            reconciliationResults = reconciliationProvider.reconcile(file1, file2);
        } catch (ReconciliationException e) {
            throw new BadRequestException("transaction.reconciliation.invalid-input", "Invalid file input", e);
        }
        log.debug("End reconciliation process with {} result", reconciliationResults.size());

        ReconciliationSummaryResponse reconciliationSummaryResponse = toConciliationOverviewResponse(reconciliationResults);
        response.setReconciliationOverview(reconciliationSummaryResponse);

        populateRecordsToResponse(response, reconciliationResults);

        return response;

    }

    private void populateRecordsToResponse(ReconciliationResultResponse response, List<ReconciliationResult> reconciliationResults) {
        List<ReconciliationResult> unmatchedRecords = new ArrayList<>();
        List<ReconciliationResult> matchingRecords = new ArrayList<>();
        List<ReconciliationResult> suggestedRecords = new ArrayList<>();

        for (ReconciliationResult r : reconciliationResults) {
            if (r.getMatchingPercentage().compareTo(Constants.ONE_HUNDRED_PERCENTAGE) == 0) {
                matchingRecords.add(r);
            } else if (r.getMatchingPercentage().compareTo(Constants.ZERO_PERCENTAGE) == 0) {
                unmatchedRecords.add(r);
            } else {
                suggestedRecords.add(r);
            }
        }

        response.setUnmatchedRecords(unmatchedRecords);
        response.setMatchingRecords(matchingRecords);
        response.setSuggestedRecords(suggestedRecords);
    }


    public ReconciliationSummaryResponse toConciliationOverviewResponse(List<ReconciliationResult> reconciliationResults) {
        Integer file1TotalCount = 0;
        Integer file1MatchingCount = 0;
        Integer file1UnmatchedCount = 0;
        Integer file1SuggestedCount = 0;
        Integer file2TotalCount = 0;
        Integer file2MatchingCount = 0;
        Integer file2UnmatchedCount = 0;
        Integer file2SuggestedCount = 0;
        for (ReconciliationResult reconciliationResult : reconciliationResults) {
            boolean isRecord1UnmatchedResult = reconciliationResult.getRecord1() == null;
            boolean isRecord2UnmatchedResult = reconciliationResult.getRecord2() == null;
            if (isRecord1UnmatchedResult) {
                file2TotalCount++;
                file2UnmatchedCount++;
            } else if (isRecord2UnmatchedResult) {
                file1TotalCount++;
                file1UnmatchedCount++;
            } else {//both record 1 and 2 present => transaction ids are matching
                boolean isPerfectMatchingResult = reconciliationResult.getMatchingPercentage().compareTo(Constants.ONE_HUNDRED_PERCENTAGE) == 0;
                boolean isSuggestedMatchingResult = reconciliationResult.getMatchingPercentage().compareTo(Constants.ONE_HUNDRED_PERCENTAGE) == -1;
                if (isPerfectMatchingResult) {
                    file1MatchingCount++;
                    file1TotalCount++;
                    file2MatchingCount++;
                    file2TotalCount++;
                } else if (isSuggestedMatchingResult) {
                    file1SuggestedCount++;
                    file1TotalCount++;
                    file2SuggestedCount++;
                    file2TotalCount++;
                }
            }
        }

        ReconciliationSummaryResponse response = new ReconciliationSummaryResponse();
        response.setFile1TotalCount(file1TotalCount);
        response.setFile1MatchingCount(file1MatchingCount);
        response.setFile1UnmatchedCount(file1UnmatchedCount);
        response.setFile1SuggestedCount(file1SuggestedCount);
        response.setFile2TotalCount(file2TotalCount);
        response.setFile2MatchingCount(file2MatchingCount);
        response.setFile2UnmatchedCount(file2UnmatchedCount);
        response.setFile2SuggestedCount(file2SuggestedCount);
        return response;
    }


}
