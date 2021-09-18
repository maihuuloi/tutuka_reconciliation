package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.parser.FileParser;

import java.util.List;

public class ReconciliationProviderBuilder {

    private List<MatchingCriteria> passRule;

    private String indexColumn;

    private FileParser fileParser;

    public ReconciliationProviderBuilder passRule(List<MatchingCriteria> passRule) {
        this.passRule = passRule;
        return this;
    }

    public ReconciliationProviderBuilder indexColumn(String indexColumn) {
        this.indexColumn = indexColumn;
        return this;
    }

    public ReconciliationProviderBuilder fileParser(FileParser fileParser) {
        this.fileParser = fileParser;
        return this;
    }

    public ReconciliationProvider build() {
        return new ReconciliationProvider(new RecordMatcher(passRule), fileParser, indexColumn);
    }
}
