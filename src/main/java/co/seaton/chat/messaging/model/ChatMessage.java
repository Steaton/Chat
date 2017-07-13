package co.seaton.chat.messaging.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {

    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private String message;

    private Date date;

    private String username;

    public ChatMessage(String message, String username) {
        this.message = message;
        this.username = username;
        this.date = new Date();
    }

    public String getDateString() {
        return dateFormat.format(date);
    }

    public String toString() {
        return username + " (" + dateFormat.format(date) + "): " + message;
    }
}
