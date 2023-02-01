package ru.kpfu.itis.gnt.client;

import ru.kpfu.itis.gnt.exceptions.ClientException;
import ru.kpfu.itis.gnt.exceptions.CloseSocketException;
import ru.kpfu.itis.gnt.models.MessageType;

public interface Client {
    boolean connect() throws ClientException, CloseSocketException;
    void disconnect() throws CloseSocketException;
    void sendMessage(String messageToSend, MessageType type) throws ClientException, CloseSocketException;
}
