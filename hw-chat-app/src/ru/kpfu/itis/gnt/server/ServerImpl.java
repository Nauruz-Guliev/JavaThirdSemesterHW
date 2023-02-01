package ru.kpfu.itis.gnt.server;

import ru.kpfu.itis.gnt.GUI.ErrorFrame;
import ru.kpfu.itis.gnt.exceptions.ServerException;
import ru.kpfu.itis.gnt.repository.StickerRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerImpl implements Server {
    private ServerSocket serverSocket;
    private final int port;

    public ServerImpl(int port) {
        this.port = port;
    }

    @Override
    public void startServer() throws ServerException {
        try {
            serverSocket = new ServerSocket(this.port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler newClient = new ClientHandler(socket);
                Thread newClientThread = new Thread(newClient);
                newClientThread.start();
                System.out.println("New user has connected");
            }
        } catch (IOException ex) {
            stopServer();
            new ErrorFrame("Couldn't start server. Cause: " + ex.getMessage());
        }
    }

    @Override
    public void stopServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
