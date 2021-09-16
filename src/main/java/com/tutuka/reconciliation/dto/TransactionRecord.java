package com.tutuka.reconciliation.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRecord {
    @CsvBindByName(column = "ProfileName")
    private String profileName;
    @CsvDate(value = "yyyy-MM-dd hh:mm:ss")
    @CsvBindByName(column = "TransactionDate")
    private Date transactionDate;
    @CsvBindByName(column = "TransactionAmount")
    private Double transactionAmount;
    @CsvBindByName(column = "TransactionNarrative")
    private String transactionNarrative;
    @CsvBindByName(column = "TransactionDescription")
    private String transactionDescription;
    @CsvBindByName(column = "TransactionID")
    private String transactionID;
    @CsvBindByName(column = "TransactionType")
    private String transactionType;
    @CsvBindByName(column = "WalletReference")
    private String walletReference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecord that = (TransactionRecord) o;
        return transactionID.equals(that.transactionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionID);
    }
}
