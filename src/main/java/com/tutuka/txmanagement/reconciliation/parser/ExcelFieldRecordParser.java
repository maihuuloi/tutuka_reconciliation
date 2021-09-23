package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.model.FieldRecord;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelFieldRecordParser<T extends FieldRecord> implements FileParser<T> {
    private final Class<T> clazz;
    private FileParser<T> parserChain;

    public ExcelFieldRecordParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> parse(File file) throws IOException, CsvException {
        String extension = FilenameUtils.getExtension(file.getName());
        boolean isExcelFile = Arrays.asList("xlsx", "xls").contains(extension);
        if(!isExcelFile) {
            if (parserChain == null) {
                throw new UnsupportedOperationException("No file parser for file with extension " + extension);
            }

            return parserChain.parse(file);
        }

        return null;//TODO: Implement excel parse
    }

    public ExcelFieldRecordParser<T> setNextParser(FileParser<T> parserChain) {
        this.parserChain = parserChain;
        return this;
    }
}
