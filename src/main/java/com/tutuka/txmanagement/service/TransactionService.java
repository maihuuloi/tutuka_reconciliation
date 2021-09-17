package com.tutuka.txmanagement.service;

import com.tutuka.txmanagement.dto.ReconciliationResultResponse;

import java.io.File;

public interface TransactionService {
    ReconciliationResultResponse reconcile(File file1, File file2);
}
