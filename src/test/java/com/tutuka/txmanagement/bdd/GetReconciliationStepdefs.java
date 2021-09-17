package com.tutuka.txmanagement.bdd;

import com.tutuka.txmanagement.dto.ReconciliationOverviewResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class GetReconciliationStepdefs extends SpringIntegrationTest {

    private File file1;
    private File file2;

    @Given("two valid transaction files set")
    public void userHaveTwoValidTransactionFiles() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/file1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/file2.csv");
    }

    @When("client call reconciliation api to reconcile these transaction files")
    public void clientUploadTwoTransactionFilesToTransactionsReconciliationOverview() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files",file1);
        body.add("files",file2);

        executeMultipartFilePost("/transactions/reconciliation-overview", body, ReconciliationOverviewResponse.class);
    }

    @Then("return valid reconciliation overview")
    public void returnReconciliationOverview() {
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ReconciliationOverviewResponse reconciliationOverviewResponse = (ReconciliationOverviewResponse) responseEntity.getBody();
        Assert.assertNotNull(reconciliationOverviewResponse.getFile1MatchingCount());
        Assert.assertNotNull(reconciliationOverviewResponse.getFile1TotalCount());
        Assert.assertNotNull(reconciliationOverviewResponse.getFile1UnmatchedCount());
    }

    @Given("two identical transaction files with duplicate id record set")
    public void twoTransactionFilesWithDuplicateIdRecordSet() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/duplicate_id_records_2.csv");
    }

    @Then("return valid reconciliation overview with equal metrics")
    public void returnValidReconciliationOverviewWithEqualMetrics() {

    }

    @Given("file one contain transaction id not match in file two")
    public void fileOneContainTransactionIdNotMatchInFileTwo() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_1.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/none_id_match_from_file1_to_file2_2.csv");
    }

    @Then("return valid number of un matched in file one greater file two")
    public void returnValidNumberOfUnMatchedInFileOneGreaterFileTwo() {

    }
}
