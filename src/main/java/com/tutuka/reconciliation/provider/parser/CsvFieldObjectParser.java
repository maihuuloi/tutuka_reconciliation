package com.tutuka.reconciliation.provider.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.tutuka.reconciliation.provider.model.FieldObjectRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CsvFieldObjectParser<T extends FieldObjectRecord> implements FileParser<T> {
    private Class<T> clazz;

    public CsvFieldObjectParser(Class<T> clazz) {
        this.clazz = clazz;
    }
    @Override
    public List<T > parse(File file) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withSeparator(',')
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
