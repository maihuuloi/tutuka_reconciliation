package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.model.Record;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileParser<T extends Record> {
    List<T> parse(File file) throws IOException, CsvException;
}
