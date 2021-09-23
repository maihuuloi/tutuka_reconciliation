package com.tutuka.txmanagement.reconciliation;

import com.tutuka.txmanagement.model.TransactionRecord;
import com.tutuka.txmanagement.reconciliation.model.FieldObjectRecord;
import com.tutuka.txmanagement.reconciliation.model.Record;
import com.tutuka.txmanagement.reconciliation.parser.CsvFieldObjectParser;
import com.tutuka.txmanagement.reconciliation.parser.ExcelFieldObjectParser;
import com.tutuka.txmanagement.reconciliation.parser.FileParser;
import com.tutuka.txmanagement.reconciliation.strategy.GreedyReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.IndexReconciliationStrategy;
import com.tutuka.txmanagement.reconciliation.strategy.ReconciliationStrategy;
import org.apache.commons.lang3.StringUtils;

import javax.activation.UnsupportedDataTypeException;
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

        return new ReconciliationProvider(strategy, fileParser());
    }

    private FileParser fileParser() {
        if (fileParser != null) {
            return fileParser;
        }

        if (FieldObjectRecord.class.isAssignableFrom(recordType)) {
            if (isSupportExcel) {
                CsvFieldObjectParser csvFieldObjectParser = new CsvFieldObjectParser(recordType);
                csvFieldObjectParser.setNextParser(new ExcelFieldObjectParser(recordType));
                return csvFieldObjectParser;
            } else {
                return new CsvFieldObjectParser(recordType);
            }
        } else {
            //TODO: implement for handling other record type
           throw new UnsupportedOperationException("Class type not supported yet");
        }

    }

}
