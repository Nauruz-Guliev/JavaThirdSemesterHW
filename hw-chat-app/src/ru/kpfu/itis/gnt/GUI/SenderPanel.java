package ru.kpfu.itis.gnt.GUI;

import ru.kpfu.itis.gnt.exceptions.RepositoryException;
import ru.kpfu.itis.gnt.repository.StickerRepository;

import javax.swing.*;
import java.awt.*;

import static ru.kpfu.itis.gnt.GUI.MessengerAppFrame.SECONDARY_BLUE_LIGHT;

public class SenderPanel extends JPanel {
    private JButton sendButton;
    private JScrollPane scrollPane;
    private JComboBox<String> comboBox;
    private JTextArea messageArea;

    private static final int DEFAULT_HEIGHT = 40;

    public SenderPanel() {
        this.setBackground(SECONDARY_BLUE_LIGHT);
        createMessageTextArea();
        createComboBox();
        createSendButton();
        this.setLayout(new BorderLayout());

        this.add(comboBox, BorderLayout.WEST);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(0, DEFAULT_HEIGHT));
        this.add(sendButton, BorderLayout.EAST);
        this.setVisible(true);
    }

    private void createMessageTextArea() {
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane = new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }


    public void createComboBox() {
        try {
            comboBox = new JComboBox<String>(StickerRepository.getStickerCodes());
        } catch (RepositoryException e) {
            new ErrorFrame(e.getMessage());
        }
        comboBox.setPreferredSize(new Dimension(80,DEFAULT_HEIGHT));
    }

    private void createSendButton() {
        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.setPreferredSize(new Dimension(100, DEFAULT_HEIGHT));
        sendButton.setFocusable(false);
    }


}
