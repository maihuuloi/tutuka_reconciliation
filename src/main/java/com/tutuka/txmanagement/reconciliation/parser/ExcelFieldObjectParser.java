package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.model.FieldObjectRecord;
import com.tutuka.txmanagement.reconciliation.model.Record;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.activation.UnsupportedDataTypeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class ExcelFieldObjectParser<T extends FieldObjectRecord> implements FileParser<T> {
    private final Class<T> clazz;
    private FileParser<T> parserChain;

    public ExcelFieldObjectParser(Class<T> clazz) {
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

    public ExcelFieldObjectParser<T> setNextParser(FileParser<T> parserChain) {
        this.parserChain = parserChain;
        return this;
    }
}
