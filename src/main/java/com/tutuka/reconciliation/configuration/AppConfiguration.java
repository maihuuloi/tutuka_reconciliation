package com.tutuka.reconciliation.configuration;

import com.tutuka.reconciliation.dto.TransactionRecord;
import com.tutuka.reconciliation.processor.CsvParser;
import com.tutuka.reconciliation.provider.ColumnNameReconciliationProvider;
import com.tutuka.reconciliation.provider.ColumnMatcherConfig;
import com.tutuka.reconciliation.provider.matcher.DateMatcher;
import com.tutuka.reconciliation.provider.matcher.NumberMatch;
import com.tutuka.reconciliation.provider.ReconciliationProvider;
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
    public ReconciliationProvider getReconciliationProvider() {
        List<ColumnMatcherConfig> columnMatcherConfigs = new ArrayList<>();
        //TODO: move to configuration file
        ColumnMatcherConfig transactionAmount = ColumnMatcherConfig.<Double>builder()
        .columnName("TransactionAmount")
        .score(1)
        .valueMatcher(new NumberMatch())
        .mustMatch(false)
        .build();

        columnMatcherConfigs.add(transactionAmount);
        ColumnMatcherConfig walletReference = ColumnMatcherConfig.<String>builder()
                .columnName("WalletReference")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .mustMatch(false)
                .build();

        columnMatcherConfigs.add(walletReference);

        ColumnMatcherConfig transactionNarrative = ColumnMatcherConfig.<String>builder()
                .columnName("TransactionNarrative")
                .score(1)
                .valueMatcher(new StringExactMatcher())
                .mustMatch(false)
                .build();
        columnMatcherConfigs.add(transactionNarrative);

        ColumnMatcherConfig transactionDate =  ColumnMatcherConfig.<Date>builder()
                .columnName("TransactionDate")
                .score(1)
                .valueMatcher(new DateMatcher())
                .mustMatch(false)
                .build();

        columnMatcherConfigs.add(transactionDate);

        ReconciliationProvider provider = new ColumnNameReconciliationProvider(columnMatcherConfigs);
        return provider;
    }

    @Bean
    public CsvParser csvParser(){
        return (file -> CsvUtils.toList(file, TransactionRecord.class));
    }
}
