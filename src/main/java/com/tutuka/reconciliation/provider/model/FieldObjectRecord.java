package com.tutuka.reconciliation.provider.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.tutuka.reconciliation.exception.ColumnNameNotFoundException;
import com.tutuka.reconciliation.provider.annotation.MatchColumnName;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

public abstract class FieldObjectRecord implements Record {


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
