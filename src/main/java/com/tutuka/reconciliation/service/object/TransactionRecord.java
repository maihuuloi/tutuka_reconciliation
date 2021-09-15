package com.tutuka.reconciliation.service.object;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TransactionRecord {
    @CsvBindByName(column = "ProfileName", required = true)
    private String profileName;
    @CsvDate(value = "yyyy-MM-dd hh:mm:ss")
    @CsvBindByName(column = "TransactionDate", required = true)
    private Date transactionDate;
    @CsvBindByName(column = "TransactionAmount", required = true)
    private Double transactionAmount;
    @CsvBindByName(column = "TransactionNarrative", required = true)
    private String transactionNarrative;
    @CsvBindByName(column = "TransactionDescription", required = true)
    private String transactionDescription;
    @CsvBindByName(column = "TransactionID", required = true)
    private String transactionID;
    @CsvBindByName(column = "TransactionType", required = true)
    private String transactionType;
    @CsvBindByName(column = "WalletReference", required = true)
    private String walletReference;

}
