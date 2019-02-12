package ru.bahusdivus.mtkauto;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainJTable extends JTable {
    private CarsJPanel carPanel;
    private StateJPanel topPanel;
    private FilterJPanel filterJPanel;
    private DbHandler dbHandler;
    private TableRowSorter<AbstractPartsAndToTableModel> tableRowSorter;

    MainJTable(CarsJPanel carPanel, StateJPanel topPanel, FilterJPanel filterJPanel) {
        this.carPanel = carPanel;
        this.topPanel = topPanel;
        this.filterJPanel = filterJPanel;

        try {
            dbHandler = DbHandler.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        populateTable();

        //Setting table properties. Height is questionable, must fill all space
        setPreferredScrollableViewportSize(new Dimension(getPreferredSize().width, 370));
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(false);

        //Setting selection mode
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //Setting selection Listeners
        SelectionHandler selectionListener = new SelectionHandler();
        getSelectionModel().addListSelectionListener(selectionListener);
        getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

        //Setting Default sorter and Date editor
        setAutoCreateRowSorter(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        setDefaultEditor(Date.class, new DateEditor());

    }

    void populateTable() {

        AbstractPartsAndToTableModel tableModel;
        if (topPanel.getActiveTopButton() != 3) {
            tableModel = new PartsTableModel(dbHandler.getParts(carPanel.getActiveCarBtn(), topPanel.getActiveTopButton()));
        } else {
            tableModel = new ToTableModel(dbHandler.getTOByCarId(carPanel.getActiveCarBtn()));
        }

        int mdlRowCount = tableModel.getRowCount();
        if (mdlRowCount > 0) {
            setModel(tableModel);
            //Movinge ID columt to the end. There will be "delete" button. May be better do it in Parts and TO classes
            moveColumn(0, getColumnCount() - 1);

            //Setting sorter
            tableRowSorter = new TableRowSorter<>(tableModel);
            setRowSorter(tableRowSorter);

            TableColumnModel tcm = getColumnModel();
            //Setting renderer and editor for last column. This convert cell into "Delete" button
            tcm.getColumn(getColumnCount() - 1).setCellRenderer(new DeleteCellRenderer());
            tcm.getColumn(getColumnCount() - 1).setCellEditor(new DeleteCellEditor());
            //Remove unnecessary columns
            if (topPanel.getActiveTopButton() != 3) tcm.removeColumn(tcm.getColumn(1));
            tcm.removeColumn(tcm.getColumn(0));
        } else {
            setModel(new DefaultTableModel());
        }


    }

    public TableRowSorter<AbstractPartsAndToTableModel> getRowSorter () {
        return tableRowSorter;
    }

    //Custom editor for columns of Date.class. Formatting text in cell with "dd.MM.yyyy" format
    class DateEditor extends DefaultCellEditor {
        JTextField textField;

        DateEditor() {
            super(new JTextField());
            textField = (JTextField) getComponent();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            textField.setText(new SimpleDateFormat("dd.MM.yyyy").format((Date) value));
            return textField;
        }

        public Object getCellEditorValue() {
            textField = (JTextField) getComponent();
            return textField.getText();
        }

    }

    //Custom renderer for "Delete" cell, converting cell into JButton
    private class DeleteCellRenderer extends JButton implements TableCellRenderer {

        DeleteCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Delete");
            return this;
        }
    }

    //Custom editor for "Delete" cell. On click fire fireEditingStopped(), cause execution setValueAt() in TableModel, wich handle deleting
    private class DeleteCellEditor extends DefaultCellEditor {
        private JButton button;
        private Object value;

        DeleteCellEditor() {
            super(new JCheckBox());

            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.value = value;
            button.setText("Delete");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return value;
        }
    }

    // Handling cells selection. If selected cells have numeric class, showing column header and summ in JLabel
    class SelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {

            filterJPanel.setResultLbl("");
            StringBuilder result = new StringBuilder();

            int[] selectedRows = getSelectedRows();
            int[] selectedColumns = getSelectedColumns();

            for (int selectedColumn : selectedColumns) {
                String header = getColumnName(selectedColumn);
                int intSumm = 0;
                float floatSumm = 0f;
                long longSumm = 0L;

                for (int selectedRow : selectedRows) {
                    Object value = getValueAt(selectedRow, selectedColumn);
                    if (value instanceof Integer) {
                        intSumm += (int) value;
                    } else if (value instanceof Float) {
                        floatSumm += (float) value;
                    } else if (value instanceof Long) {
                        longSumm += (long) value;
                    } else {
                        header = "";
                    }
                }
                if (!header.equals("")) {
                    result.append(header).append(": ");
                    if (intSumm > 0) result.append(intSumm).append(" ");
                    if (floatSumm > 0) result.append(floatSumm).append(" ");
                    if (longSumm > 0) result.append(longSumm).append(" ");
                }
            }
            filterJPanel.setResultLbl(result.toString());
        }
    }



}
