package by.morf.server;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageObj implements Serializable {
    private String sender;
    private String senderId;
    private String text;
    private Timestamp time;

    public MessageObj(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
