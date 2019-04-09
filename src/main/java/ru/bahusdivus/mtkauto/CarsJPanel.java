package ru.bahusdivus.mtkauto;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CarsJPanel extends JPanel {

    private Map<Integer, JButton> carButtons = new HashMap<>();
    private DbHandler dbHandler;
    private int activeCarBtn;
    private ActionListener listener;

    CarsJPanel(ActionListener listener) {
        this.listener = listener;
        try {
            dbHandler = DbHandler.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        makeCarButtons();
    }

    void makeCarButtons () {
        removeAll();
        carButtons.clear();
        activeCarBtn = 0;

        List<Car> cars = dbHandler.getCars();
        for (Car car : cars) {
            JButton button = new JButton(car.getNumber());
            button.addActionListener(listener);
            button.setActionCommand(Integer.toString(car.getId()));
            add(button);
            carButtons.put(car.getId(), button);
            if (activeCarBtn == 0) {
                button.setEnabled(false);
                activeCarBtn = car.getId();
            }
        }

        revalidate();
        repaint();
    }

    void setActiveCarBtn(int btn) {
        carButtons.get(activeCarBtn).setEnabled(true);
        activeCarBtn = btn;
        carButtons.get(activeCarBtn).setEnabled(false);
    }

    int getActiveCarBtn () {
        return activeCarBtn;
    }

    void deleteActiveCar() {
        dbHandler.deleteCar(activeCarBtn);
        makeCarButtons();
    }

}
