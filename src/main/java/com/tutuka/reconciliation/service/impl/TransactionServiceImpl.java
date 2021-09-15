package com.tutuka.reconciliation.service.impl;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;
import com.tutuka.reconciliation.exception.BadRequestException;
import com.tutuka.reconciliation.service.TransactionService;
import com.tutuka.reconciliation.service.object.TransactionRecord;
import com.tutuka.reconciliation.util.CsvUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public ReconciliationOverviewResponse getConciliationOverview(File file1, File file2) {
        List<TransactionRecord> file1TransactionRecords = null;
        List<TransactionRecord> file2TransactionRecords;
        try {
            file1TransactionRecords = CsvUtils.toList(file1.toPath(), TransactionRecord.class);
            file2TransactionRecords = CsvUtils.toList(file1.toPath(), TransactionRecord.class);

        } catch (CsvException e) {
            throw new BadRequestException("transaction.invalid-format-file");
        } catch (IOException e) {
            //TODO:
//            throw e;
        }
        for (TransactionRecord file1TransactionRecord : file1TransactionRecords) {

        }
        return null;
    }
}
