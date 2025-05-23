package com.tutuka.txmanagement.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.tutuka.txmanagement.reconciliation.annotation.MatchColumnName;
import com.tutuka.txmanagement.reconciliation.model.FieldRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecord extends FieldRecord {

    @CsvBindByName(column = "ProfileName")
    @MatchColumnName(name = "ProfileName")
    private String profileName;

    @CsvDate(value = "yyyy-MM-dd hh:mm:ss")
    @CsvBindByName(column = "TransactionDate")
    @MatchColumnName(name = "TransactionDate")
    private Date transactionDate;

    @CsvBindByName(column = "TransactionAmount")
    @MatchColumnName(name = "TransactionAmount")
    private Double transactionAmount;

    @CsvBindByName(column = "TransactionNarrative")
    @MatchColumnName(name = "TransactionNarrative")
    private String transactionNarrative;

    @CsvBindByName(column = "TransactionDescription")
    @MatchColumnName(name = "TransactionDescription")
    private String transactionDescription;

    @CsvBindByName(column = "TransactionID")
    @MatchColumnName(name = "TransactionID")
    private String transactionID;

    @CsvBindByName(column = "TransactionType")
    @MatchColumnName(name = "TransactionType")
    private String transactionType;

    @CsvBindByName(column = "WalletReference")
    @MatchColumnName(name = "WalletReference")
    private String walletReference;
}
