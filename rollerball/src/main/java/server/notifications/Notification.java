package server.notifications;

import java.time.LocalDateTime;
import java.util.Date;

public class Notification {
    private String message;
    private LocalDateTime date;
    private boolean unread;
    private String type;

    public Notification(String message, LocalDateTime date, boolean unread, String type){
        this.message = message;
        this.date = date;
        this.unread = unread;
        this.type = type;
    }
}
