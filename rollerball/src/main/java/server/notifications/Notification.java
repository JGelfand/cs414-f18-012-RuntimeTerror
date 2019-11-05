package server.notifications;

import java.time.LocalDateTime;
import java.util.Date;

public class Notification {
    public String message;
    public LocalDateTime date;
    public boolean unread;
    private String type;
    public Integer sender;
    public String senderUsername;
    public int id;

    public Notification(String message, LocalDateTime date, boolean unread, String type, int id){
        this.message = message;
        this.date = date;
        this.unread = unread;
        this.type = type;
        this.id = id;
    }

    public Notification(String message, LocalDateTime date, boolean unread, String type, int sender, String senderUsername, int id){
        this(message, date, unread, type, id);
        this.sender = sender;
        this.senderUsername = senderUsername;
    }
}
