package com.tutuka.txmanagement.reconciliation;

import com.opencsv.bean.CsvBindByName;
import com.tutuka.txmanagement.reconciliation.annotation.MatchColumnName;
import com.tutuka.txmanagement.reconciliation.model.FieldRecord;
import lombok.Data;

@Data
public class TestFieldRecord extends FieldRecord {

    @CsvBindByName(column = "TransactionAmount")
    @MatchColumnName(name = "TransactionAmount")
    private Double transactionAmount;

    @CsvBindByName(column = "TransactionID")
    @MatchColumnName(name = "TransactionID")
    private String transactionID;
}
