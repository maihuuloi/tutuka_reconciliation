package com.tutuka.reconciliation.provider.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.provider.TransactionRecord;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileParser {
    List<TransactionRecord> parse(File file) throws IOException, CsvException;
}
