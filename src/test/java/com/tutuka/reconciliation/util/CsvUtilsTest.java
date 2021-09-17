package com.tutuka.reconciliation.util;

import com.tutuka.reconciliation.provider.model.Record;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

class CsvUtilsTest {

    @Test
    public void toList_WhenValidFile_ThenReturnListOfBean() throws Exception {
        File file1 = ResourceUtils.getFile("classpath:testfile/file1.csv");
        List<Record> records = CsvUtils.toList(file1, Record.class);

        Assert.assertNotNull(records);
        Assert.assertFalse(records.isEmpty());
        System.out.println(records.get(0));
    }

}
