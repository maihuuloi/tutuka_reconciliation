package com.tutuka.reconciliation.service.impl;

import com.tutuka.reconciliation.configuration.ReconciliationProviderConfiguration;
import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;
import com.tutuka.reconciliation.service.TransactionService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


@ContextConfiguration(classes = {TransactionServiceImpl.class, ReconciliationProviderConfiguration.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    public void getReconciliationOverview_WhenFilesValid_ThenReturnReconciliationOverview() throws FileNotFoundException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/file1.csv");
        File file2 = ResourceUtils.getFile("classpath:testfile/file2.csv");

        //Act
        ReconciliationOverviewResponse response = transactionService.reconcile(file1, file2).getReconciliationOverview();

        //Assert
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getFile1TotalCount());
        Assert.assertNotNull(response.getFile1UnmatchedCount());
        Assert.assertNotNull(response.getFile1MatchingCount());
    }

    public void getReconciliationOverview_WhenTransactionIdNotfoundInOtherFile_ThenReturnTheseRecordAsUnMatched() throws FileNotFoundException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/file1.csv");
        File file2 = ResourceUtils.getFile("classpath:testfile/file2.csv");

        //Act
        ReconciliationOverviewResponse response = transactionService.reconcile(file1, file2).getReconciliationOverview();

        //Assert
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getFile2MatchingCount());
        Assert.assertNotNull(response.getFile2MatchingCount());
        Assert.assertNotNull(response.getFile2MatchingCount());

    }

    @Test
    public void getReconciliationOverview_WhenNoneIdMatchRecordsInFileOne_ThenReturnDifferentTotal() throws FileNotFoundException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_1.csv");
        File file2 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_2.csv");

        //Act
        ReconciliationOverviewResponse response = transactionService.reconcile(file1, file2).getReconciliationOverview();

        //Assert
        Assert.assertNotSame(response.getFile1TotalCount() ,  response.getFile2TotalCount());
    }

    @Test
    public void getReconciliationOverview_WhenNoneIdMatchRecordsInFileTwo_ThenReturnDifferentTotal() throws FileNotFoundException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file2_to_file1_1.csv");
        File file2 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file2_to_file1_2.csv");

        //Act
        ReconciliationOverviewResponse response = transactionService.reconcile(file1, file2).getReconciliationOverview();

        //Assert
        Assert.assertNotSame(response.getFile1TotalCount() ,  response.getFile2TotalCount());
    }

    @Test
    public void getReconciliationOverview_WhenDuplicateIdRecordsInFileOne_ThenReturnSameResultForTwoFiles() throws FileNotFoundException {
        //Arrange
        File file1 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_1.csv");
        File file2 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_2.csv");

        //Act
        ReconciliationOverviewResponse response = transactionService.reconcile(file1, file2).getReconciliationOverview();

        //Assert
        Assert.assertSame(response.getFile1TotalCount(), response.getFile2TotalCount());
        Assert.assertSame(response.getFile1MatchingCount(), response.getFile2MatchingCount());
        Assert.assertSame(response.getFile1UnmatchedCount(), response.getFile2UnmatchedCount());
    }


}
