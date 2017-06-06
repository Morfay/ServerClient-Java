package by.morf.server;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.ServerSocket;

public class Server{
    static ArrayList<ListenerThread> clientList;
    static LinkedBlockingQueue<Object> messages;

    public static void main(String[] args) {
        clientList = new ArrayList<>();
        messages = new LinkedBlockingQueue<>();

        try {
            new Thread(new MessagesThread()).start();

            ServerSocket server = new ServerSocket(5678, 10);
            while (true) {
                ListenerThread connectionHandler = new ListenerThread(server.accept());

                clientList.add(connectionHandler);
                new Thread(connectionHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
