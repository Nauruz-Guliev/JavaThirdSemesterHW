package ru.kpfu.itis.gnt.client;


import ru.kpfu.itis.gnt.exceptions.ClientException;
import ru.kpfu.itis.gnt.exceptions.CloseSocketException;
import ru.kpfu.itis.gnt.exceptions.MessageException;
import ru.kpfu.itis.gnt.utils.StringConverter;
import ru.kpfu.itis.gnt.models.Message;
import ru.kpfu.itis.gnt.models.MessageType;
import ru.kpfu.itis.gnt.utils.ErrorHandler;

import java.io.*;
import java.net.Socket;

public class ClientImpl implements Client {
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String userName;
    private boolean isConnected;
    private final int port;


    public ClientImpl(int port) {
        this.port = port;
        this.isConnected = false;
    }

    public void setName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean connect() throws ClientException, CloseSocketException {
        try {
            if (!isConnected) {
                socket = new Socket("localhost", port);
                this.outputStream = socket.getOutputStream();
                this.inputStream = socket.getInputStream();
            } else {
                System.out.println("Already connected");
            }
            isConnected = true;
        } catch (IOException e) {
            disconnect();
            throw new ClientException("Couldn't connect due to: " + e.getMessage());
        }
        return isConnected;
    }

    @Override
    public void disconnect() throws CloseSocketException {
        ErrorHandler.closeSocket(socket);
    }


    @Override
    public void sendMessage(String messageToSend, MessageType type) throws ClientException, CloseSocketException {
        try {
            Message message;
            if (type == MessageType.USERNAME || type == MessageType.STICKER) {
                message = Message.createMessage(type, StringConverter.encode(messageToSend));
            } else {
                message = Message.createMessage(type, StringConverter.encode(userName + ": " + messageToSend));
            }
            outputStream.write(Message.getBytes(message));
            outputStream.flush();
        } catch (IOException | MessageException ex) {
            disconnect();
            throw new ClientException("Couldn't send the message due to: " + ex.getMessage());
        }
    }


    public String getUserName() {
        return userName;
    }

    public Socket getSocket() {
        return socket;
    }
}
