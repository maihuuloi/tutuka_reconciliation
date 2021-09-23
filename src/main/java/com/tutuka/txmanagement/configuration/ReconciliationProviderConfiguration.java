package com.tutuka.txmanagement.configuration;

import com.tutuka.txmanagement.model.TransactionRecord;
import com.tutuka.txmanagement.reconciliation.MatchingCriteria;
import com.tutuka.txmanagement.reconciliation.ReconciliationProvider;
import com.tutuka.txmanagement.reconciliation.matcher.DateRangeMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.EqualMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.StringSimilarMatcher;
import com.tutuka.txmanagement.reconciliation.parser.CsvFieldObjectParser;
import com.tutuka.txmanagement.reconciliation.parser.ExcelFieldObjectParser;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class ReconciliationProviderConfiguration {

    @Bean
    public ReconciliationProvider reconciliationProvider() {
        return ReconciliationProvider.builder()
                .passRule(getPassRule())
                .recordType(TransactionRecord.class)
                .indexColumn("TransactionID")
                .supportExcel()
                .build();
    }


    //TODO: a run time configuration for pass rule
    private List<MatchingCriteria> getPassRule() {
        List<MatchingCriteria> matchingCriteria = new ArrayList<>();
        MatchingCriteria transactionID = MatchingCriteria.<String>builder()
                .columnName("TransactionID")
                .score(4)
                .valueMatcher(new EqualMatcher<>())
                .build();
        matchingCriteria.add(transactionID);

        MatchingCriteria transactionAmount = MatchingCriteria.<Double>builder()
                .columnName("TransactionAmount")
                .score(1)
                .valueMatcher(new EqualMatcher<>())
                .build();

        matchingCriteria.add(transactionAmount);
        MatchingCriteria walletReference = MatchingCriteria.<String>builder()
                .columnName("WalletReference")
                .score(1)
                .valueMatcher(new EqualMatcher<>())
                .build();

        matchingCriteria.add(walletReference);

        MatchingCriteria transactionNarrative = MatchingCriteria.<String>builder()
                .columnName("TransactionNarrative")
                .score(1)
                .valueMatcher(new EqualMatcher<>())
                .build();
        matchingCriteria.add(transactionNarrative);

        MatchingCriteria transactionDescription = MatchingCriteria.<String>builder()
                .columnName("TransactionDescription")
                .score(1)
                .valueMatcher(new StringSimilarMatcher())
                .build();
        matchingCriteria.add(transactionDescription);

        MatchingCriteria transactionDate = MatchingCriteria.<Date>builder()
                .columnName("TransactionDate")
                .score(1)
                .valueMatcher(new DateRangeMatcher())
                .build();

        matchingCriteria.add(transactionDate);

        return matchingCriteria;
    }

}
