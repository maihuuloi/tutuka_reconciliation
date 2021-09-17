package com.tutuka.reconciliation.provider.model;

import java.util.HashMap;
import java.util.Map;

public class ColumnMapRecord implements Record {
    private Map<String, String> cells = new HashMap<>();

    @Override
    public Object getValueByColumnName(String columnName) {
        return cells.get(columnName);
    }

    public void setValue(String header, String cell) {
        cells.put(header,cell);
    }
}
