package com.tutuka.reconciliation.provider.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.provider.TransactionRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

public class CsvParser implements FileParser {
    @Override
    public List<TransactionRecord> parse(File file) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            CsvToBean<TransactionRecord> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(TransactionRecord.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
