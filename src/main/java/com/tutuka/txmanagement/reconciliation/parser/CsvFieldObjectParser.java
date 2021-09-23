package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderNameBaseMappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.tutuka.txmanagement.reconciliation.model.FieldObjectRecord;
import org.apache.commons.io.FilenameUtils;

import javax.activation.UnsupportedDataTypeException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

public class CsvFieldObjectParser<T extends FieldObjectRecord> implements FileParser<T> {
    private Class<T> clazz;
    private FileParser parserChain;

    public CsvFieldObjectParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> parse(File file) throws IOException, CsvException {
        String extension = FilenameUtils.getExtension(file.getName());

        boolean isCsvFile = "csv".equals(extension);
        if (!isCsvFile) {
            if (parserChain == null) {
                throw new UnsupportedOperationException("No file parser for file with extension " + extension);
            }

            return parserChain.parse(file);
        }

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
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .build();

            return csvToBean.parse();
        }
    }

    public void setNextParser(FileParser parserChain) {
        this.parserChain = parserChain;
    }
}
