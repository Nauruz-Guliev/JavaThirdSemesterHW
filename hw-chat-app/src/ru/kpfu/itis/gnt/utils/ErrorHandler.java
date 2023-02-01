package ru.kpfu.itis.gnt.utils;

import ru.kpfu.itis.gnt.GUI.ErrorFrame;
import ru.kpfu.itis.gnt.exceptions.CloseSocketException;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class ErrorHandler {
    public static void closeApp(Socket socket, String message) throws CloseSocketException {
        closeSocket(socket);
        new ErrorFrame(message);
    }

    public static <T extends Frame> void closeApp(Socket socket, String message, T frame) throws CloseSocketException {
        closeSocket(socket);
        new ErrorFrame(message, frame);
    }

    /* В документации написанно, что закрытие сокета также закрывает input и output stream'ы */
    public static void closeSocket(Socket socket) throws CloseSocketException {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new CloseSocketException("Couldn't close the socket due to" + e.getMessage());
        }
    }
}
