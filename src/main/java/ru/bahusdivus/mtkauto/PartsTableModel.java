package ru.bahusdivus.mtkauto;

import java.util.List;

class PartsTableModel extends AbstractPartsAndToTableModel {

    private String[] columnNames = {"",
            "Car ID",
            "Type",
            "Номенклатура",
            "Количество",
            "Стоимость",
            "Поставщик",
            "Дата установки",
            "Пробег, км"};

    private List<Part> data;
    private DbHandler dbHandler;

    PartsTableModel(List<Part> list) {
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
        if (data == null) return 0;
        else return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        if (getRowCount() == 0) return null;
        else return data.get(row).getPropertyByN(col);
    }

    public void setValueAt(Object value, int row, int col) {
        //Little cheat here. "col == 0" mean, we got "Delete" button pressed. So, instead saving, we deleting
        if (col == 0) {
            dbHandler.deletePart((int) value);
            data.remove(row);
            fireTableRowsDeleted(row, row);
        } else {
            data.get(row).setPropertyByN(col, value);
            fireTableCellUpdated(row, col);
            dbHandler.savePart(data.get(row));
        }
    }

    public void addRow(Object part) {
        data.add((Part) part);
        dbHandler.savePart((Part) part);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}
