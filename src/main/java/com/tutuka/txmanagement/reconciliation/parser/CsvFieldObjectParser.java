package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderNameBaseMappingStrategy;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tutuka.txmanagement.reconciliation.model.FieldObjectRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

public class CsvFieldObjectParser<T extends FieldObjectRecord> implements FileParser<T> {
    private Class<T> clazz;

    public CsvFieldObjectParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> parse(File file) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            HeaderNameBaseMappingStrategy mappingStrategy = new HeaderColumnNameMappingStrategy() {
                @Override
                public void verifyLineLength(int numberOfFields) throws CsvRequiredFieldEmptyException {
                    //super.verifyLineLength(numberOfFields);
                }
            };
            mappingStrategy.setType(clazz);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withMappingStrategy(mappingStrategy)
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
