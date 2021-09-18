package com.tutuka.txmanagement.bdd;

import com.tutuka.txmanagement.dto.ReconciliationSummaryResponse;
import com.tutuka.txmanagement.dto.ReconciliationResultResponse;
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

    @Given("two transaction files with valid records")
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

    @Given("two identical transaction files with duplicate value in transaction id column")
    public void twoTransactionFilesWithDuplicateIdRecordSet() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_2.csv");
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

    @Then("return valid number of un matched in file one greater file two")
    public void returnValidNumberOfUnMatchedInFileOneGreaterFileTwo() {
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ReconciliationSummaryResponse reconciliationSummaryResponse = (ReconciliationSummaryResponse) responseEntity.getBody();
        Assert.assertNotNull(reconciliationSummaryResponse.getFile1MatchingCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile1TotalCount());
        Assert.assertNotNull(reconciliationSummaryResponse.getFile1UnmatchedCount());
    }

    @Then("the client receives status code of {int}")
    public void theClientReceivesStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, responseEntity.getStatusCode().value());
    }
}
