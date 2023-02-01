package ru.kpfu.itis.gnt;

import ru.kpfu.itis.gnt.GUI.ErrorFrame;
import ru.kpfu.itis.gnt.exceptions.ServerException;
import ru.kpfu.itis.gnt.server.ServerImpl;

import java.util.HashMap;

public class ServerApp {

    private static ServerImpl server;
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            init();
            server.startServer();
        } catch (ServerException e) {
            new ErrorFrame(e.getMessage());
        }
    }

    private static void init() {
        server = new ServerImpl(PORT);
    }

}
