package com.tutuka.txmanagement.bdd;

import com.tutuka.txmanagement.dto.ReconciliationSummaryResponse;
import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class GetReconciliationStepdefs extends SpringIntegrationTest {

    private File file1;
    private File file2;

    @Given("two transaction files with matching, unmatched, and suggested records")
    public void userHaveTwoValidTransactionFiles() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/valid_records_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/valid_records_2.csv");
    }

    @When("client call reconciliation api to reconcile these transaction files")
    public void clientUploadTwoTransactionFilesToTransactionsReconciliationOverview() {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files",new FileSystemResource(file1));
        body.add("files",new FileSystemResource(file2));

        executeMultipartFilePost("/transactions/reconcile", body, ReconciliationResultResponse.class);
    }

    @Then("the client receives reconciliation with result summary")
    public void returnReconciliationOverview() {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertNotNull(reconciliationSummaryResponse.getFile1MatchingCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile1TotalCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile1UnmatchedCount());

        Assert.assertNotNull(reconciliationSummaryResponse.getFile2MatchingCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile2TotalCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile2UnmatchedCount());
    }

    @Given("two identical transaction files with duplicate value in TransactionID column")
    public void twoTransactionFilesWithDuplicateIdRecordSet() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records.csv");
    }

    @Then("the client receives reconciliation result with equal metrics in summary")
    public void returnValidReconciliationOverviewWithEqualMetrics() {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse summary = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(summary);
    }

    @Given("file one contain transaction id not match in file two")
    public void fileOneContainTransactionIdNotMatchInFileTwo() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_2.csv");
    }

    @Given("file one contain one record has only TransactionAmount matched with file two")
    public void fileOneContainOneRecordHasOnlyTransactionAmountMatchedWithFileTwo() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/only_transaction_amount_matched_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/only_transaction_amount_matched_2.csv");

    }

    @Given("two file with invalid CSV file")
    public void twoFileWithInvalidCSVFileFormat() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/invalid_file.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/invalid_file.csv");

    }

    @Given("two file with records having TransactionID empty")
    public void twoFileWithRecordsHaveTransactionIDEmpty() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/transaction_id_empty.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/transaction_id_empty.csv");
    }


    @Given("two file with records have TransactionDate empty")
    public void twoFileWithRecordsHaveTransactionDateEmpty() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/transaction_date_empty.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/transaction_date_empty.csv");
    }

    @Then("the client receives status code of {int}")
    public void theClientReceivesStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, responseEntity.getStatusCode().value());
    }

    @And("the client receives at least {int} matching record")
    public void theClientReceivesAtLeastMatchingRecord(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);
        Assert.assertTrue(reconciliationResultResponse.getMatchingRecords().size() >= count);
    }

    @And("the client receives at least {int} unmatched record")
    public void theClientReceivesAtLeastUnmatchedRecord(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertTrue(reconciliationResultResponse.getUnmatchedRecords().size() >= count);
    }

    @And("the client receives at least {int} suggested record")
    public void theClientReceivesAtLeastSuggestedRecord(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertTrue(reconciliationResultResponse.getSuggestedRecords().size() >= count);
    }

    @And("the client receives {int} unmatched records")
    public void theClientReceivesUnmatchedRecords(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertTrue(reconciliationResultResponse.getUnmatchedRecords().size() == count);
    }

    @And("the client receives {int} suggested records")
    public void theClientReceivesSuggestedRecords(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertTrue(reconciliationResultResponse.getSuggestedRecords().size() == count);
    }

    @And("the client receives {int} matching records")
    public void theClientReceivesMatchingRecords(int count) {
        ReconciliationResultResponse reconciliationResultResponse = (ReconciliationResultResponse) responseEntity.getBody();
        ReconciliationSummaryResponse reconciliationSummaryResponse = reconciliationResultResponse.getReconciliationOverview();
        Assert.assertNotNull(reconciliationSummaryResponse);

        Assert.assertTrue(reconciliationResultResponse.getMatchingRecords().size() == count);
    }

}
