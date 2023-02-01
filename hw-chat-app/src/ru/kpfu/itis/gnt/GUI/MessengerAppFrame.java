package ru.kpfu.itis.gnt.GUI;

import ru.kpfu.itis.gnt.client.ClientImpl;
import ru.kpfu.itis.gnt.exceptions.ClientException;
import ru.kpfu.itis.gnt.exceptions.CloseSocketException;
import ru.kpfu.itis.gnt.exceptions.MessageException;
import ru.kpfu.itis.gnt.exceptions.RepositoryException;
import ru.kpfu.itis.gnt.models.Message;
import ru.kpfu.itis.gnt.models.MessageType;
import ru.kpfu.itis.gnt.repository.StickerRepository;
import ru.kpfu.itis.gnt.utils.ErrorHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MessengerAppFrame extends JFrame {

    protected ChatPanel chatPanel;
    protected SenderPanel senderPanel;

    private ClientImpl client;

    private String[] stickerCodes;


    public MessengerAppFrame(ClientImpl client) {
        try {
            this.client = client;
            this.setTitle("CHAT");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            this.setLayout(new BorderLayout());
            chatPanel = new ChatPanel();
            senderPanel = new SenderPanel();
            this.add(chatPanel, BorderLayout.CENTER);
            this.add(senderPanel, BorderLayout.PAGE_END);
            this.setVisible(true);
            initSendClickListener();
            initComboBoxListener();
            listenForMessages();
            stickerCodes = StickerRepository.getStickerCodes();
        } catch (RepositoryException e) {
            // если стикеров нет в репо, это не значит, что окно надо закрывать и использовать чат нельзя, поэтому весь фрейм не передаю
            new ErrorFrame(e.getMessage());
        }
    }


    private void initSendClickListener() {
        this.senderPanel.getSendButton().addActionListener(e -> {
            JTextArea panel = this.senderPanel.getMessageArea();
            String text = panel.getText();
            if (!text.equals("")) {
                try {
                    client.sendMessage(text, MessageType.REGULAR_MESSAGE);
                } catch (ClientException | CloseSocketException ex) {
                    try {
                        ErrorHandler.closeApp(client.getSocket(), ex.getMessage(), this);
                    } catch (CloseSocketException exc) {
                        this.closeWindow();
                    }
                }
                panel.setText("");
            }
        });
    }

    private void initComboBoxListener() {
        this.senderPanel.getComboBox().addActionListener(
                e -> sendSticker(stickerCodes[senderPanel.getComboBox().getSelectedIndex()])
        );
    }

    private void sendSticker(String stickerCode) {
        new Thread(
                () -> {
                    try {
                        client.sendMessage(stickerCode, MessageType.STICKER);
                    } catch (ClientException | CloseSocketException ex) {
                        try {
                            ErrorHandler.closeApp(client.getSocket(), ex.getMessage());
                        } catch (CloseSocketException e) {
                            this.closeWindow();
                        }
                    }
                }
        ).start();
    }

    public void listenForMessages() {
        // слушаем сообщения на отдельном потоке
        new Thread(() -> {
            String messageFromGroupChat;
            while (client.getSocket().isConnected()) {
                try {
                    Message newMessage = Message.readMessage(client.getSocket().getInputStream());
                    if (newMessage.getType() == MessageType.STICKER.getType()) {
                        //если сообщение - стикер, мы его отображаем
                        InputStream imageBytes = Message.getImageInputStream(newMessage);
                        displaySticker(imageBytes);
                    } else if (newMessage.getType() == MessageType.ERROR_MESSAGE.getType()) {
                        // если ошибки со стороны сервера, то мы их выводим в окошке и выходим из цикла
                        ErrorHandler.closeApp(client.getSocket(), Message.toString(newMessage), this);
                        closeWindow();
                        break;
                    } else {
                        //в остальных случаях отображаем в чате
                        messageFromGroupChat = Message.toString(newMessage);
                        displayMessage(messageFromGroupChat);
                    }
                } catch (IOException | CloseSocketException | MessageException e) {
                    // если вдруг не получилось вывести окно с исключением и закрыть сокет, то просто закрываем окошко
                    try {
                        ErrorHandler.closeApp(client.getSocket(), e.getMessage(), this);
                    } catch (CloseSocketException ex) {
                        this.closeWindow();
                    }
                }
            }
        }).start();
    }


    public void displayMessage(final String messageToDisplay) {
        //операции, которые не связаны с отрисовкой UI лучше делать на отдельном потоке
        new Thread(
                () -> {
                    if (messageToDisplay.startsWith(client.getUserName())) {
                        String newMessageToDisplay = messageToDisplay.replaceFirst(client.getUserName(), "You");
                        this.chatPanel.addMessageLabel(newMessageToDisplay);
                    } else {
                        this.chatPanel.addMessageLabel(messageToDisplay);
                    }
                }
        ).start();
    }

    public void displaySticker(InputStream stickerStream) throws IOException {
        this.chatPanel.addImage(stickerStream);
    }

    private void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }


    public static final int WINDOW_HEIGHT = 520;
    public static final int WINDOW_WIDTH = 400;
    public static final Color PRIMARY_COLOR = new Color(235, 242, 250);
    public static final Color SECONDARY_GREEN = new Color(103, 148, 54);
    public static final Color SECONDARY_GREEN_LIGHT = new Color(165, 190, 0);
    public static final Color SECONDARY_BLUE = new Color(5, 102, 141);
    public static final Color SECONDARY_BLUE_LIGHT = new Color(104, 162, 200);
}
