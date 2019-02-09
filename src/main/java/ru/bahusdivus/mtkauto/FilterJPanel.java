package ru.bahusdivus.mtkauto;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FilterJPanel extends JPanel {
    private JTextField fromDate;
    private JTextField toDate;
    private JLabel resultLbl;

    FilterJPanel(DocumentListener listener) {
        setLayout(new FlowLayout(FlowLayout.LEADING));

        // Filters work only together. Doesn't work if not filled properly
        // "After" filter
        JLabel fromDateLabel = new JLabel("Дата с:");
        fromDate = new JTextField();
        fromDate.setColumns(10);
        fromDate.getDocument().addDocumentListener(listener);

        // "Before" filter
        JLabel toDateLabel = new JLabel(" по: ");
        toDate = new JTextField();
        toDate.setColumns(10);
        toDate.getDocument().addDocumentListener(listener);

        add(fromDateLabel);
        add(fromDate);
        add(toDateLabel);
        add(toDate);

        resultLbl = new JLabel();
        add(resultLbl);
    }

    String getFromText() {
        return fromDate.getText();

    }

    String getToText() {
        return toDate.getText();
    }

    void setResultLbl(String text) {
        resultLbl.setText(text);
    }
}
