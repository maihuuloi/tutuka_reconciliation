package com.tutuka.reconciliation.provider;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.tutuka.reconciliation.exception.ColumnNameNotFoundException;
import com.tutuka.reconciliation.provider.annotation.MatchColumnName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
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
    private String transactionId;

    @CsvBindByName(column = "TransactionType")
    @MatchColumnName(name = "TransactionType")
    private String transactionType;

    @CsvBindByName(column = "WalletReference")
    @MatchColumnName(name = "WalletReference")
    private String walletReference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecord that = (TransactionRecord) o;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }


    public Object getValueByColumnName(String columnName) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            MatchColumnName annotation = field.getAnnotation(MatchColumnName.class);
            String column = annotation.name();
            if (column.equals(columnName)) {
                try {
                    return field.get(this);
                } catch (IllegalAccessException e) {
                    //ignore
                }
            }
            field.setAccessible(false);
        }

        throw new ColumnNameNotFoundException("Column with name " + columnName + " not found");
    }
}
