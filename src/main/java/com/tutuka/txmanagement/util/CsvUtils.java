package com.tutuka.txmanagement.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

public class CsvUtils {
    public static <T> List<T> toList(File file, Class<T> clazz) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(clazz)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            return csvToBean.parse();
        }
    }

}
