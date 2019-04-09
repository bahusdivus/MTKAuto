package ru.bahusdivus.mtkauto;

import javax.swing.table.DefaultTableModel;

public abstract class AbstractPartsAndToTableModel extends DefaultTableModel {

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }


    public abstract void addRow(Object object);
}
