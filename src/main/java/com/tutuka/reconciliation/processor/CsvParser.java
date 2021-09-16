package com.tutuka.reconciliation.processor;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.dto.TransactionRecord;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CsvParser {
    List<TransactionRecord> parse(File file) throws IOException, CsvException;
}
