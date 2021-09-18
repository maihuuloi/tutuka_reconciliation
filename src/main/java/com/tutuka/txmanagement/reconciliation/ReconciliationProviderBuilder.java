package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import com.tutuka.txmanagement.reconciliation.strategy.GreedyReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.IndexReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.ReconciliationStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ReconciliationProviderBuilder {

    private List<MatchingCriteria> passRule;

    private String indexColumn;

    private FileParser fileParser;

    public ReconciliationProviderBuilder passRule(List<MatchingCriteria> passRule) {
        this.passRule = passRule;
        return this;
    }
    /**
     * @see IndexReconciliationStrategy#indexColumn
     * @param indexColumn Please see the "See Also" section
     * @return {@code this}
     */
    public ReconciliationProviderBuilder indexColumn(String indexColumn) {
        this.indexColumn = indexColumn;
        return this;
    }

    public ReconciliationProviderBuilder fileParser(FileParser fileParser) {
        this.fileParser = fileParser;
        return this;
    }

    public ReconciliationProvider build() {
        ReconciliationStrategy strategy;
        RecordMatcher recordMatcher = new RecordMatcher(passRule);
        if(StringUtils.isNotEmpty(indexColumn)) {
            strategy = new IndexReconciliationStrategy(indexColumn, recordMatcher);
        } else {
            strategy =new GreedyReconciliationStrategy(recordMatcher);
        }
        return new ReconciliationProvider(strategy, fileParser);
    }
}
