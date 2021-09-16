package com.tutuka.reconciliation.util;

import com.tutuka.reconciliation.dto.TransactionRecord;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

class CsvUtilsTest {

    @Test
    public void toList_WhenValidFile_ThenReturnListOfBean() throws Exception {
        File file1 = ResourceUtils.getFile("classpath:testfile/file1.csv");
        List<TransactionRecord> transactionRecords = CsvUtils.toList(file1, TransactionRecord.class);

        Assert.assertNotNull(transactionRecords);
        Assert.assertFalse(transactionRecords.isEmpty());
        System.out.println(transactionRecords.get(0));
    }

}
