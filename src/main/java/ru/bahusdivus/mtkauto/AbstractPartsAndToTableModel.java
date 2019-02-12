package ru.bahusdivus.mtkauto;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractPartsAndToTableModel extends AbstractTableModel {

    public abstract int getColumnCount();

    public abstract int getRowCount();

    public abstract String getColumnName(int col);

    public abstract Object getValueAt(int row, int col);

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public abstract void setValueAt(Object value, int row, int col);

    public abstract void addRow(Object object);
}
