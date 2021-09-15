package com.tutuka.reconciliation.bdd;

import com.tutuka.reconciliation.dto.ReconciliationOverviewResponse;
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

    @Given("user have two valid transaction files")
    public void userHaveTwoValidTransactionFiles() throws FileNotFoundException {
        file1 = ResourceUtils.getFile("classpath:testfile/clientmarkofffile20140113.csv");
        file2 = ResourceUtils.getFile("classpath:testfile/tutukamarkofffile20140113.csv");

    }

    @When("client upload two transaction files to transactions reconciliation-overview api")
    public void clientUploadTwoTransactionFilesToTransactionsReconciliationOverview() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files",file1);
        body.add("files",file2);

        executeMultipartFilePost("/transactions/reconciliation-overview", body, ReconciliationOverviewResponse.class);
    }

    @Then("return reconciliation overview")
    public void returnReconciliationOverview() {
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ReconciliationOverviewResponse reconciliationOverviewResponse = (ReconciliationOverviewResponse) responseEntity.getBody();
        Assert.assertNotNull(reconciliationOverviewResponse.getTotalRecord());
        Assert.assertNotNull(reconciliationOverviewResponse.getMatchingRecord());
        Assert.assertNotNull(reconciliationOverviewResponse.getUnmatchedRecord());
    }

}
