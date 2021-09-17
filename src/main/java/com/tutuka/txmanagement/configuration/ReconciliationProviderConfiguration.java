package com.tutuka.txmanagement.configuration;

import com.tutuka.txmanagement.model.TransactionRecord;
import com.tutuka.txmanagement.reconciliation.MatchingCriteria;
import com.tutuka.txmanagement.reconciliation.ReconciliationProvider;
import com.tutuka.txmanagement.reconciliation.matcher.DateRangeMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.NumberMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.OneToOneRecordMatcher;
import com.tutuka.txmanagement.reconciliation.matcher.RecordMatcher;
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
        return new ReconciliationProvider(getReconciliationProvider()
                , fileParser()
                , "TransactionID") ;
    }

    @Bean
    public FileParser fileParser() {
        return new CsvFieldObjectParser(TransactionRecord.class);
    }

    @Bean
    public RecordMatcher getReconciliationProvider() {
        List<MatchingCriteria> matchingCriteria = new ArrayList<>();
        //TODO: move to configuration file or provider user an interface
        MatchingCriteria transactionAmount = MatchingCriteria.<Double>builder()
                .columnName("TransactionAmount")
                .score(1)
                .valueMatcher(new NumberMatcher())
                .mustMatch(false)
                .build();

        matchingCriteria.add(transactionAmount);
        MatchingCriteria walletReference = MatchingCriteria.<String>builder()
                .columnName("WalletReference")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .mustMatch(false)
                .build();

        matchingCriteria.add(walletReference);

        MatchingCriteria transactionNarrative = MatchingCriteria.<String>builder()
                .columnName("TransactionNarrative")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .mustMatch(false)
                .build();
        matchingCriteria.add(transactionNarrative);

        MatchingCriteria transactionDate = MatchingCriteria.<Date>builder()
                .columnName("TransactionDate")
                .score(1)
                .valueMatcher(new DateRangeMatcher())
                .mustMatch(false)
                .build();

        matchingCriteria.add(transactionDate);

        RecordMatcher provider = new OneToOneRecordMatcher(matchingCriteria);
        return provider;
    }

}
