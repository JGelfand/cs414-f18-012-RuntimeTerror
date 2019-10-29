package server.notifications;

import java.time.LocalDateTime;
import java.util.Date;

public class Notification {
    private String message;
    private LocalDateTime date;
    private boolean unread;

    public Notification(String message, LocalDateTime date, boolean unread){
        this.message = message;
        this.date = date;
        this.unread = unread;
    }
}
