package by.morf.client;

import by.morf.server.MessageObj;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame implements Runnable, ActionListener {
    static private Socket connection;
    static private ObjectOutputStream output;
    static private ObjectInputStream input;
    static private String name;
    static private String server;
    private MessageObj message;
    private final JTextArea ta1;
    private final JTextField t1;
    private final JButton b1;

    public Client(String title) {
        super(title);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        t1 = new JTextField(17);
        b1 = new JButton("Send");
        ta1 = new JTextArea(14,20);
        ta1.setEditable(false);
        ta1.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) ta1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane sp = new JScrollPane(ta1);

        b1.addActionListener(this);
        t1.addActionListener(this);

        add(sp);
        add(t1);
        add(b1);

        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeConnection();

                super.windowClosing(event);
            }
        });
    }

    public static void main(String[] args) {
        new Thread(new Client("Chat")).start();
    }

    public void run() {
        server = JOptionPane.showInputDialog(null, "Enter server address:", "127.0.0.1:5678");
        String[] ipPort = server.split(":");

        try {
            connection = new Socket(InetAddress.getByName(ipPort[0]), Integer.parseInt(ipPort[1]));
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            name = JOptionPane.showInputDialog(null, "Enter your name:", "Guest");

            while ((message = (MessageObj) input.readObject()) != null) {
                ta1.append(message.sender + ": " + message.text);
            }
        } catch (ConnectException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection();
        }
    }

    private static void sendData(Object text) {
        try {
            MessageObj data = new MessageObj();
            data.sender = name;
            data.text = text + "\n\r";

            output.writeObject(data);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == b1 || e.getSource() == t1) && !t1.getText().equals("")) {
            sendData(t1.getText());
            t1.setText("");
        }
    }
}
