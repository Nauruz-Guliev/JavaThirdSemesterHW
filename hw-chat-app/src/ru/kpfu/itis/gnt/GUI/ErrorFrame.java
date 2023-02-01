package ru.kpfu.itis.gnt.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static ru.kpfu.itis.gnt.GUI.MessengerAppFrame.SECONDARY_BLUE_LIGHT;

public class ErrorFrame extends JFrame {
    private JLabel errorLabel;
    private String errorMessage;


    /* необходимо закрывать сам компонент, из которого конструктор был вызван */
    public <T extends Frame> ErrorFrame(String errorMessage, T frame) {
        initFrame(errorMessage);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                frame.setVisible(false);
                frame.dispose();
                e.getWindow().dispose();
                System.exit(0);
            }
        });
    }
    /* не всегда вызывается из существуеющего GUI */
    public ErrorFrame(String errorMessage) {
        initFrame(errorMessage);
    }

    private void initFrame(String errorMessage) {
        this.errorMessage = errorMessage;
        this.setBackground(SECONDARY_BLUE_LIGHT);
        this.setLayout(new BorderLayout());
        createErrorLabel();
        this.add(errorLabel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(400, 100));
        this.pack();
        this.setVisible(true);
    }


    private void createErrorLabel() {
        errorLabel = new JLabel(errorMessage);
    }
}
