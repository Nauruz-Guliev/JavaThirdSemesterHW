package ru.kpfu.itis.gnt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private final String fileDirectory;
    private final int port;

    private final static String DIRECTORY_PATH = "src\\files";
    private final static int PORT = 1234;

    public static void main(String[] args) throws ServerException {
        FileServer server = new FileServer(PORT, DIRECTORY_PATH);
        server.startServer();
    }


    public FileServer(int port, String fileDirectory) {
        this.fileDirectory = fileDirectory;
        this.port = port;
    }

    public void startServer() throws ServerException {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                new Thread(
                        new Client(server.accept(), fileDirectory)
                ).start();
                System.out.println("Connected");
            }
        } catch (IOException ex) {
            throw new ServerException("Couldn't start the server.");
        }
    }

}
