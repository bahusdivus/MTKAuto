package ru.bahusdivus.mtkauto;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public abstract class AbstractPartsAndToTableModel extends AbstractTableModel {

    private String[] columnNames = null;
    private List<TO> data = null;

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row).getPropertyByN(col);
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        childSetValueAt(value, row, col);
    }

    public abstract void childSetValueAt (Object value, int row, int col);
}
