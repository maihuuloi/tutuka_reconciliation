package com.tutuka.reconciliation.provider.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.provider.model.Record;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileParser<T extends Record> {
    List<T> parse(File file) throws IOException, CsvException;
}
