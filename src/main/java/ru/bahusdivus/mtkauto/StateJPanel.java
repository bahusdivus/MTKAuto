package ru.bahusdivus.mtkauto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

class StateJPanel extends JPanel {

    private JButton[] topButtons = new JButton[4];
    private int currentButton = 0;

    StateJPanel (ActionListener listener) {
        setLayout(new GridLayout(1, 4));

        topButtons[0] = new JButton("Ремонт тягачей и п/прицепов");
        topButtons[0].setEnabled(false);
        topButtons[1] = new JButton("Шины");
        topButtons[2] = new JButton("АКБ");
        topButtons[3] = new JButton("ТО");

        for (int i = 0; i < topButtons.length; i++) {
            topButtons[i].setActionCommand(String.valueOf(i));
            topButtons[i].addActionListener(listener);
            add(topButtons[i]);
        }

    }

    void setActiveTopButton(int btn) {
        topButtons[currentButton].setEnabled(true);
        currentButton = btn;
        topButtons[currentButton].setEnabled(false);
    }

    int getActiveTopButton() {
        return currentButton;
    }

}
