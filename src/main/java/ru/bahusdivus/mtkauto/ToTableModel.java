package ru.bahusdivus.mtkauto;

import java.util.List;

class ToTableModel extends AbstractPartsAndToTableModel {

    private String[] columnNames = {"",
            "Car ID",
            "Наименование",
            "Марка",
            "Дата замены",
            "Пробег, км",
            "Следующая замена, км"};

    private List<TO> data;
    private DbHandler dbHandler;

    ToTableModel(List<TO> list) {
        data = list;
        try {
            dbHandler = DbHandler.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void setValueAt(Object value, int row, int col) {
        //Little cheat here. "col == 0" mean, we got "Delete" button pressed. So, instead saving, we deleting
        if (col == 0) {
            dbHandler.deleteTO((int) value);
            data.remove(row);
            fireTableRowsDeleted(row, row);
        } else {
            data.get(row).setPropertyByN(col, value);
            fireTableCellUpdated(row, col);
            dbHandler.saveTO(data.get(row));
        }
    }

    public void addRow(Object to) {
        data.add((TO) to);
        dbHandler.saveTO((TO) to);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}
