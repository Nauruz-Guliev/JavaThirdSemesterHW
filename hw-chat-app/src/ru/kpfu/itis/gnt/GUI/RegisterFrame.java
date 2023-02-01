package ru.kpfu.itis.gnt.GUI;

import ru.kpfu.itis.gnt.client.ClientImpl;
import ru.kpfu.itis.gnt.exceptions.ClientException;
import ru.kpfu.itis.gnt.exceptions.CloseSocketException;
import ru.kpfu.itis.gnt.exceptions.RegistrationException;
import ru.kpfu.itis.gnt.models.MessageType;
import ru.kpfu.itis.gnt.utils.ErrorHandler;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JButton registerButton;
    private JTextField textField;
    private final ClientImpl client;

    public RegisterFrame(ClientImpl client) {
        this.client = client;
        this.setTitle("Register");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 100);
        this.setLayout(new BorderLayout());
        createRegisterButton();
        createMessageTextArea();
        this.add(registerButton, BorderLayout.EAST);
        this.add(textField, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void createRegisterButton() {
        registerButton = new JButton();
        registerButton.setText("Register");
        registerButton.setFocusable(false);
        registerButton.addActionListener(
                e -> registerInChat()
        );
    }

    private void createMessageTextArea() {
        textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void registerInChat() {
        try {
            String userName = textField.getText();
            if (!userName.equals("")) {
                client.setName(userName);
                if (client.connect()) {
                    openMainWindow(client);
                    client.sendMessage(userName, MessageType.USERNAME);
                } else {
                    client.disconnect();
                    throw new RegistrationException("Couldn't connect the client");
                }
                closeRegisterWindow();
            } else {
                //можно уведомлять как-то лучше пользователя, но целое окошко выводить - было бы слишком
                System.out.println("Empty message");
            }
        } catch (ClientException | CloseSocketException | RegistrationException ex) {
            new ErrorFrame(ex.getMessage(), this);
        }
    }

    private void closeRegisterWindow() {
        this.setVisible(false);
        this.dispose();
    }

    private void openMainWindow(ClientImpl client) {
        new MessengerAppFrame(client);
    }
}


