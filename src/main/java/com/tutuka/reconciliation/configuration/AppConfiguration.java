package com.tutuka.reconciliation.configuration;

import com.tutuka.reconciliation.provider.TransactionRecord;
import com.tutuka.reconciliation.provider.parser.FileParser;
import com.tutuka.reconciliation.provider.OneToOneRecordMatcher;
import com.tutuka.reconciliation.provider.MatchingCriteria;
import com.tutuka.reconciliation.provider.matcher.DateRangeMatcher;
import com.tutuka.reconciliation.provider.matcher.NumberMatch;
import com.tutuka.reconciliation.provider.RecordMatcher;
import com.tutuka.reconciliation.provider.matcher.StringExactMatcher;
import com.tutuka.reconciliation.util.CsvUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class AppConfiguration {

    @Bean
    public RecordMatcher getReconciliationProvider() {
        List<MatchingCriteria> matchingCriteria = new ArrayList<>();
        //TODO: move to configuration file
        MatchingCriteria transactionAmount = MatchingCriteria.<Double>builder()
        .columnName("TransactionAmount")
        .score(1)
        .valueMatcher(new NumberMatch())
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

        MatchingCriteria transactionDate =  MatchingCriteria.<Date>builder()
                .columnName("TransactionDate")
                .score(1)
                .valueMatcher(new DateRangeMatcher())
                .mustMatch(false)
                .build();

        matchingCriteria.add(transactionDate);

        RecordMatcher provider = new OneToOneRecordMatcher(matchingCriteria);
        return provider;
    }

    @Bean
    public FileParser fileParser(){
        return (file -> CsvUtils.toList(file, TransactionRecord.class));
    }
}
