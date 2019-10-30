package server.notifications;

import server.accounts.Account;
import server.accounts.AccountManager;
import server.utils.DatabaseHelper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationManager {
    public static List<Notification> getRecentOrUnreadNotifications(int accountId){
        List<Notification> notifications= new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE recipient = ? AND (unread IS TRUE OR TIMESTAMPDIFF(DAY, time, NOW()) < 30) ;";
        try(DatabaseHelper helper = DatabaseHelper.create()){
            boolean success = helper.executePreparedStatement(query,(ResultSet results) -> {
                while (results.next()){
                    String message = results.getString("message");
                    String type = results.getString("type");
                    Timestamp timestamp = results.getTimestamp("time");
                    LocalDateTime date = timestamp.toLocalDateTime();
                    boolean unread = results.getBoolean("unread");
                    notifications.add(new Notification(message, date, unread, type));
                }
                return true;
            },accountId);
            query = "SELECT * FROM invites WHERE recipient = ? AND (unread IS TRUE OR TIMESTAMPDIFF(DAY, time, NOW()) < 30) ;";
            helper.executePreparedStatement(query, (ResultSet results) -> {
                while (results.next()) {
                    String message = results.getString("message");
                    String type = results.getString("type");
                    Timestamp timestamp = results.getTimestamp("time");
                    LocalDateTime date = timestamp.toLocalDateTime();
                    boolean unread = results.getBoolean("unread");
                    int sender = results.getInt("sender");
                    String senderUsername = AccountManager.getAccountById(helper, sender);
                    notifications.add(new Notification(message, date, unread, type, sender, senderUsername));
                }return null;
            }, accountId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}