package by.morf.server;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ListenerThread implements Runnable {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean isAlive = false;

    public ListenerThread(Socket socket) {
        connection = socket;
    }

    public void run() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            isAlive = true;

            Object data;
            while ((data = input.readObject()) != null) {
                Server.messages.put(Server.clientList.indexOf(this) + ": " + data);
            }
        } catch (EOFException e) {
            // Client quit for any reason
            isAlive = false;
            Server.clientList.remove(Server.clientList.indexOf(this));
            Thread.currentThread().interrupt();
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

    public boolean isAlive() {
        boolean alive = false;
        try {
            alive = (isAlive && connection.isConnected() && !connection.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alive;
    }
}
