package com.tutuka.reconciliation.service;

import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;
import com.tutuka.reconciliation.dto.ReconciliationResultResponse;

import java.io.File;

public interface TransactionService {
    ReconciliationResultResponse reconcile(File file1, File file2);
}
