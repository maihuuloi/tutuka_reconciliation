package com.tutuka.txmanagement.provider.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.provider.model.Record;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileParser<T extends Record> {
    List<T> parse(File file) throws IOException, CsvException;
}
