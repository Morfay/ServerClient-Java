package by.morf.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ListenerThread implements Runnable {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ListenerThread(Socket socket) {
        connection = socket;
    }

    public void run() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            Object data;
            while (true) {
                data = input.readObject();
                Server.messages.put(Server.clientList.indexOf(this) + ": " + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(Object data) {
        try {
            output.writeObject(data);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
