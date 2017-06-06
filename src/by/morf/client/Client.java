package by.morf.client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame implements Runnable {
    static private Socket connection;
    static private ObjectOutputStream output;
    static private ObjectInputStream input;
    private final JTextArea ta1;

    public Client(String title) {
        super(title);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        final JTextField t1 = new JTextField(17);
        final JButton b1 = new JButton("Send");
        ta1 = new JTextArea(14,20);
        ta1.setEditable(false);
        JScrollPane sp = new JScrollPane(ta1);


        b1.addActionListener(e -> {
            if (e.getSource() == b1 && !t1.getText().equals("")) {
                sendData(t1.getText());
                t1.setText("");
            }
        });

        add(sp);
        add(t1);
        add(b1);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Thread(new Client("Chat")).start();
    }

    public void run() {
        try {
            connection = new Socket(InetAddress.getByName("127.0.0.1"), 5678);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            Object data;
            while (true) {
                data = input.readObject();
                ta1.append(data.toString());
            }
        } catch (ConnectException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally{
            try {
                output.close();
                input.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendData(Object data) {
        try {
            output.writeObject(data + "\n\r");
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
