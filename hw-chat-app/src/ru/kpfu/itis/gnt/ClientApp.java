package ru.kpfu.itis.gnt;

import ru.kpfu.itis.gnt.GUI.RegisterFrame;
import ru.kpfu.itis.gnt.client.ClientImpl;

public class ClientApp {
    private static final int PORT = 1234;
    /*
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String userName = scanner.nextLine();
        ClientImpl client = new ClientImpl(1234, userName);
        client.connect();
        client.listenForMessage();
        client.sendMessage();
    }
     */

    public static void main(String[] args) {
        ClientImpl client = new ClientImpl(PORT);
        new RegisterFrame(client);
    }
}
