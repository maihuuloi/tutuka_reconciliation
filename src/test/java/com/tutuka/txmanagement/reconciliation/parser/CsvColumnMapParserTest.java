package com.tutuka.txmanagement.reconciliation.parser;

import com.opencsv.exceptions.CsvException;
import com.tutuka.txmanagement.reconciliation.model.MapRecord;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

class CsvColumnMapParserTest {
    CsvColumnMapParser parser = new CsvColumnMapParser();

    @Test
    public void parse_WhenFileValidFormat_ThenReturnMappedRecords() throws IOException, CsvException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/csv_valid_file.csv");

        //Act
        List<MapRecord> parse = parser.parse(file1);

        //Assert
        Assert.assertNotNull(parse);
        Assert.assertFalse(parse.isEmpty());
        Object transactionID = parse.get(0).getValueByColumnName("TransactionID");
        Assert.assertNotNull(transactionID);

    }
    @Test
    public void parse_WhenHeaderAndRowMisMatchTrailingSeparator_ThenReturnMappedRecordsWithoutEmptyHeaderColumn() throws IOException, CsvException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/csv_mismatch_trailing_separator_file.csv");

        //Act
        List<MapRecord> parse = parser.parse(file1);

        //Assert
        Assert.assertNotNull(parse);
        Assert.assertFalse(parse.isEmpty());
        Object transactionID = parse.get(0).getValueByColumnName("TransactionID");
        Assert.assertNotNull(transactionID);
        Object empty = parse.get(0).getValueByColumnName("");
        Assert.assertNull(empty);
    }
}
