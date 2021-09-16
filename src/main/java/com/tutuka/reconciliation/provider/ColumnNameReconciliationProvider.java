package com.tutuka.reconciliation.provider;

import com.opencsv.bean.CsvBindByName;
import com.tutuka.reconciliation.dto.TransactionRecord;
import com.tutuka.reconciliation.exception.ColumnNameNotFoundException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;


public class ColumnNameReconciliationProvider implements ReconciliationProvider {
    List<ColumnMatcherConfig> columnMatcherConfigs;

    public ColumnNameReconciliationProvider(List<ColumnMatcherConfig> columnMatcherConfigs) {
        if (columnMatcherConfigs == null || columnMatcherConfigs.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.columnMatcherConfigs = columnMatcherConfigs;
    }

    @Override
    public MatchingResult calculate(TransactionRecord record1, TransactionRecord record2) {
        if (!record1.getTransactionID().equals(record2.getTransactionID())) {
            throw new IllegalArgumentException("Transaction id must be matched");
        }
        MatchingResult matchingResult = new MatchingResult();
        Integer matchScore = 0;
        for (ColumnMatcherConfig columnMatcherConfig : columnMatcherConfigs) {
            Object value1 = getValueByColumnName(columnMatcherConfig.getColumnName(), record1);
            Object value2 = getValueByColumnName(columnMatcherConfig.getColumnName(), record2);
            boolean matched = columnMatcherConfig.getValueMatcher().match(value1, value2);

            if (matched) {
                matchScore += columnMatcherConfig.getScore();
            } else {
                matchingResult.getUnmatchedColumns().add(columnMatcherConfig.getColumnName());

                if (columnMatcherConfig.isMustMatch()) {
                    matchScore = 0;
                    break;
                }
            }

        }

        BigDecimal matchingPercentage = new BigDecimal(matchScore).divide(new BigDecimal(getTotalScore()));
        matchingResult.setMatchingPercentage(matchingPercentage);

        return matchingResult;
    }

    public Integer getTotalScore() {
        Integer totalScore = 0;
        for (ColumnMatcherConfig columnMatcherConfig : columnMatcherConfigs) {
            totalScore += columnMatcherConfig.getScore();
        }
        return totalScore;
    }

    private Object getValueByColumnName(String columnName, TransactionRecord record) {
        Field[] fields = record.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            String column = annotation.column();
            if (column.equals(columnName)) {
                try {
                    return field.get(record);
                } catch (IllegalAccessException e) {
                    //ignore
                }
            }
            field.setAccessible(false);
        }

        throw new ColumnNameNotFoundException("Column with name " + columnName + " not found");
    }
}
