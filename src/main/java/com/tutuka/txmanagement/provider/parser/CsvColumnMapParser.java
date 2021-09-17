package com.tutuka.txmanagement.provider.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.provider.model.ColumnMapRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CsvColumnMapParser implements FileParser<ColumnMapRecord> {
    private static final String DELIMITER = ",";

    @Override
    public List<ColumnMapRecord> parse(File file) throws IOException, CsvException {
        List<ColumnMapRecord> rs =new ArrayList<>();
        List<String> lines = Files.readAllLines(file.toPath());
        String headerLine = lines.get(0);
        String[] headers = parseHeader(headerLine);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            ColumnMapRecord record =new ColumnMapRecord();
            String[] cells = line.split(DELIMITER);
            for (int j = 0; j < cells.length; j++) {
                record.setValue(headers[j], cells[j]);
            }
            rs.add(record);
        }
        return rs;
    }

    private String[] parseHeader(String headerLine) {
        String[] headers = headerLine.split(DELIMITER);
        return headers;
    }
}
