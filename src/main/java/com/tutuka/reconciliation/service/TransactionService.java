package com.tutuka.reconciliation.service;

import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;

import java.io.File;

public interface TransactionService {
    ReconciliationOverviewResponse getConciliationOverview(File file1, File file2);
}
