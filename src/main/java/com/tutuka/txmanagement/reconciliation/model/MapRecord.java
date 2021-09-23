package com.tutuka.txmanagement.reconciliation.model;

import java.util.HashMap;
import java.util.Map;

public class MapRecord implements Record {
    private Map<String, String> cells = new HashMap<>();

    @Override
    public Object getValueByColumnName(String columnName) {
        return cells.get(columnName);
    }

    public void setValue(String header, String cell) {
        cells.put(header,cell);
    }
}
