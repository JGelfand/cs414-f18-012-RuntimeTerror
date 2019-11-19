package server.notifications;

import server.accounts.Account;
import server.accounts.AccountManager;
import server.api.MessageRequest;
import server.api.MessageResponse;
import server.utils.DatabaseHelper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    public static List<Notification> getRecentOrUnreadNotifications(int accountId){
        List<Notification> notifications= new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE recipient = ? AND (unread IS TRUE OR TIMESTAMPDIFF(DAY, time, NOW()) < 1) ORDER BY time DESC;";
        try(DatabaseHelper helper = DatabaseHelper.create()){
            boolean success = helper.executePreparedStatement(query,(ResultSet results) -> {
                while (results.next()){
                    String message = results.getString("message");
                    String type = results.getString("type");
                    Timestamp timestamp = results.getTimestamp("time");
                    LocalDateTime date = timestamp.toLocalDateTime();
                    boolean unread = results.getBoolean("unread");
                    int id = results.getInt("id");
                    if(!type.equals("alert")) {
                        int sender = results.getInt("sender");
                        String senderUsername = new Account(sender).getUsername();
                        notifications.add(new Notification(message, date, unread, type, sender, senderUsername, id));
                    }else{
                        notifications.add(new Notification(message, date, unread, type, id));
                    }
                }
                return true;
            },accountId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static MessageResponse sendMessage(MessageRequest request){
        MessageResponse response = new MessageResponse();
        response.success= false;
        response.errorMessage = "";
        try(DatabaseHelper helper = DatabaseHelper.create()){
            Account account = AccountManager.getAccountByUsername(helper, request.recipient);
            if (account == null){
                response.errorMessage +="Invalid username";
            }
            if(request.message.length()>5000)
                response.errorMessage +="Message too long";
            if(!(request.type.equals("message") || request.type.equals("invite")))
                response.errorMessage +="Invalid message type";
            if(response.errorMessage.isEmpty()){
                helper.executePreparedStatement("INSERT INTO notifications(sender, recipient, type, message) VALUES (?, ?, ?, ?);",
                        request.getAccountId(), account.getAccountId(), request.type, request.message);
                response.success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}