package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.reconciliation.model.FieldRecord;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.CsvFieldRecordParser;
import com.tutuka.txmanagement.reconciliation.parser.ExcelFieldRecordParser;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import com.tutuka.txmanagement.reconciliation.strategy.GreedyReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.IndexReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.ReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.RecordMatcher;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ReconciliationProviderBuilder {

    private List<MatchingCriteria> passRule;

    private String indexColumn;

    private Class recordType;

    private boolean isSupportExcel;

    private FileParser fileParser;

    public ReconciliationProviderBuilder passRule(List<MatchingCriteria> passRule) {
        this.passRule = passRule;
        return this;
    }

    public <R extends Record> ReconciliationProviderBuilder recordType(Class<R> recordClass) {
        this.recordType = recordClass;
        return this;
    }

    public ReconciliationProviderBuilder supportExcel() {
        this.isSupportExcel = true;
        return this;
    }

    /**
     * @param indexColumn Please see the "See Also" section
     * @return {@code this}
     * @see IndexReconciliationStrategy#indexColumn
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
        if (StringUtils.isNotEmpty(indexColumn)) {
            strategy = new IndexReconciliationStrategy(indexColumn, recordMatcher);
        } else {
            strategy = new GreedyReconciliationStrategy(recordMatcher);
        }

        FileParser fileParser = fileParser();

        return new ReconciliationProvider(strategy, fileParser);
    }

    private FileParser fileParser() {
        if (fileParser != null) {
            return fileParser;
        }

        if (FieldRecord.class.isAssignableFrom(recordType)) {
            if (isSupportExcel) {
                CsvFieldRecordParser csvFieldRecordParser = new CsvFieldRecordParser(recordType);
                csvFieldRecordParser.setNextParser(new ExcelFieldRecordParser(recordType));
                return csvFieldRecordParser;
            } else {
                return new CsvFieldRecordParser(recordType);
            }
        } else {
            //TODO: implement for handling other record type
           throw new UnsupportedOperationException("Class type not supported yet");
        }

    }

}
