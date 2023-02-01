package ru.kpfu.itis.gnt.server;

import ru.kpfu.itis.gnt.exceptions.MessageException;
import ru.kpfu.itis.gnt.exceptions.RepositoryException;
import ru.kpfu.itis.gnt.models.Sticker;
import ru.kpfu.itis.gnt.repository.StickerRepository;
import ru.kpfu.itis.gnt.utils.StringConverter;
import ru.kpfu.itis.gnt.GUI.ErrorFrame;
import ru.kpfu.itis.gnt.models.Message;
import ru.kpfu.itis.gnt.models.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;
    private String clientName;



    public ClientHandler(Socket socket) throws UnsupportedEncodingException {
        try {
            this.socket = socket;
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            closeApp("Couldn't create message handler due to : " + e.getMessage());
        }
    }

    private boolean handleNewClient() {
        try {
            Message message = Message.readMessage(inputStream);
            if (message.getType() == MessageType.USERNAME.getType()) {
                this.clientName = Message.toString(message);
            }
            for (ClientHandler client : clients) {
                if (client.getClientName().equals(clientName)) {
                    Message newErrorMessage = Message.createMessage(MessageType.ERROR_MESSAGE,
                            StringConverter.encode("User with such a username already exists!")
                    );
                    sendServerMessage(newErrorMessage);
                    return false;
                }
            }
            clients.add(this);
            Message newMessage = Message.createMessage(MessageType.REGULAR_MESSAGE,
                    StringConverter.encode("SERVER: " + clientName + " has entered the chat")
            );

            broadcastMessage(Message.getBytes(newMessage));
        } catch (IOException e) {
            closeApp("Couldn't create message handler due to : " + e.getMessage());
        } catch (MessageException e) {
            closeApp(e.getMessage());
        }
        return true;
    }

    private void broadcastMessage(byte[] rawMessage) {
        for (ClientHandler client : clients) {
            try {
                client.outputStream.write(rawMessage);
                client.outputStream.flush();
            } catch (IOException e) {
                closeApp("Couldn't send the broadcast due to: " + e.getMessage());
            }
        }
    }

    private void sendServerMessage(Message message) throws IOException, MessageException {
        this.outputStream.write(Message.getBytes(message));
    }


    // достаточно закрыть сокет, стримы закрывать не нужно согласно документации
    private void closeApp(String errorMessage) {
        try {
            removeClient();
            socket.close();
        } catch (IOException | MessageException e) {
            new ErrorFrame("Error closing the app : " + e.getMessage());
        }
        new ErrorFrame(errorMessage);

    }

    public String getClientName() {
        return clientName;
    }

    public void removeClient() throws UnsupportedEncodingException, MessageException {
        clients.remove(this);
        Message newMessage = Message.createMessage(MessageType.REGULAR_MESSAGE,
                StringConverter.encode("SERVER: " + clientName + " has left the chat!")
        );
        broadcastMessage(Message.getBytes(newMessage));
    }

    @Override
    public void run() {
        if (handleNewClient()) {
            while (socket.isConnected()) {
                try {
                    handleMessages();
                } catch (IOException | MessageException e) {
                    //отключаем пользователя даже если просто неправильный тип сообщения был отправлен или же привышен размер
                    //можно не отключать и показывать окошко с ошибкой, но тогда их создасться огромное количество
                    //так как мы в бесконечном цикле
                    //пусть пользователь просто отключается. на будущее будет знать и больше ошибок не повторит
                    closeApp("User " + clientName + " has been disconnected. Cause" + e.getMessage());
                    break;
                }
            }
        }
    }

    private void handleMessages() throws IOException, MessageException {
        Message messageFromClient = Message.readMessage(inputStream);
        if (messageFromClient.getType() == MessageType.STICKER.getType()) {
            try {
                Sticker sticker = StickerRepository.getSticker(Message.toString(messageFromClient));
                Message newStickerMessage = Message.createMessage(
                        MessageType.STICKER,
                        sticker.stickerBytes()
                );
                broadcastMessage(Message.getBytes(newStickerMessage));
            } catch (RepositoryException ex) {
                Message errorMessage = Message.createMessage(
                        MessageType.REGULAR_MESSAGE,
                        StringConverter.encode(ex.getMessage())
                );
                sendServerMessage(errorMessage);
            }
        } else {
            broadcastMessage(Message.getBytes(messageFromClient));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientHandler that)) return false;
        return clientName.equals(that.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName);
    }
}
