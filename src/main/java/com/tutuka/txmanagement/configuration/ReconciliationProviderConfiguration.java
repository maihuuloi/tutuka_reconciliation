package com.tutuka.txmanagement.configuration;

import com.tutuka.txmanagement.model.TransactionRecord;
import com.tutuka.txmanagement.reconciliation.MatchingConfig;
import com.tutuka.txmanagement.reconciliation.ReconciliationProvider;
import com.tutuka.txmanagement.reconciliation.matcher.DateRangeMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.NumberMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.StringExactMatcher;
import com.tutuka.txmanagement.reconciliation.parser.CsvFieldObjectParser;
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
        return new ReconciliationProvider(getPassRule()
                , fileParser()) ;
    }

    @Bean
    public FileParser fileParser() {
        return new CsvFieldObjectParser(TransactionRecord.class);
    }

    //TODO: move to configuration file or provider user an interface
    private List<MatchingConfig> getPassRule() {
        List<MatchingConfig> matchingCriteria = new ArrayList<>();
        MatchingConfig transactionID = MatchingConfig.<String>builder()
                .columnName("TransactionID")
                .score(4)
                .valueMatcher(new StringExactMatcher())
                .index(true)
                .build();
        matchingCriteria.add(transactionID);

        MatchingConfig transactionAmount = MatchingConfig.<Double>builder()
                .columnName("TransactionAmount")
                .score(1)
                .valueMatcher(new NumberMatcher())
                .build();

        matchingCriteria.add(transactionAmount);
        MatchingConfig walletReference = MatchingConfig.<String>builder()
                .columnName("WalletReference")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .build();

        matchingCriteria.add(walletReference);

        MatchingConfig transactionNarrative = MatchingConfig.<String>builder()
                .columnName("TransactionNarrative")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .build();
        matchingCriteria.add(transactionNarrative);

        MatchingConfig transactionDate = MatchingConfig.<Date>builder()
                .columnName("TransactionDate")
                .score(1)
                .valueMatcher(new DateRangeMatcher())
                .build();

        matchingCriteria.add(transactionDate);

        return matchingCriteria;
    }

}
