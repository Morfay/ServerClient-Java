package by.morf.server;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;

public class ListenerThread implements Runnable {
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean isAlive = false;
    private HelloObj client;

    ListenerThread(Socket socket) {
        this.connection = socket;
    }

    public void run() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            isAlive = true;

            client = (HelloObj) input.readObject();
            Server.messages.put(new MessageObj(client.getName() + " connected."));

            MessageObj message;
            while ((message = (MessageObj) input.readObject()) != null) {
                message.setSenderId(client.getId());
                message.setSender(client.getName());
                message.setTime(new Timestamp(System.currentTimeMillis()));

                Server.messages.put(message);
            }
        } catch (EOFException e) {
            // Client quit for any reason
            isAlive = false;
            Server.clientList.remove(Server.clientList.indexOf(this));
            try {
                Server.messages.put(new MessageObj(client.getName() + " disconnected."));
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData(MessageObj data) {
        try {
            output.writeObject(data);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isAlive() {
        boolean alive = false;
        try {
            alive = (isAlive && connection.isConnected() && !connection.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alive;
    }
}
