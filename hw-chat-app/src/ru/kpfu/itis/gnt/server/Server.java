package ru.kpfu.itis.gnt.server;

import ru.kpfu.itis.gnt.exceptions.ServerException;

public interface Server {
    void startServer() throws ServerException;
    void stopServer();
}
