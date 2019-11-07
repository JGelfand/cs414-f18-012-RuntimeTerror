package server.matches;

import server.accounts.Account;
import server.api.InviteAnswer;
import server.notifications.Notification;
import server.utils.DatabaseHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MatchManager {
    public static Match createMatchFromInvite(InviteAnswer answer){
        Match match = null;
        try(DatabaseHelper helper = DatabaseHelper.create()){
            Notification invite = helper.executeStatement("SELECT * FROM notifications WHERE id = "+answer.inviteId+";", (results)->{
                if (results.next()) {
                    String message = results.getString("message");
                    String type = results.getString("type");
                    Timestamp timestamp = results.getTimestamp("time");
                    LocalDateTime date = timestamp.toLocalDateTime();
                    boolean unread = results.getBoolean("unread");
                    int sender = results.getInt("sender");
                    String senderUsername = new Account(sender).getUsername();
                    int id = results.getInt("id");
                    return new Notification(message, date, unread, type, sender, senderUsername, id);
                }
                return null;
            } );
            if(invite == null)
                return null;
            if(answer.accept){
                match= Match.createNewMatch(answer.inviteId, invite.sender, answer.getAccountId());
                insertMatchIntoDatabase(match, helper);
                helper.executePreparedStatement("INSERT INTO notifications(recipient, type, message) VALUES (?,\"alert\",?);",
                        invite.sender, new Account(answer.getAccountId()).getUsername()+" has accepted your invitation. Match id is "+match.getId()+".");
            }
            else{
                helper.executePreparedStatement("INSERT INTO notifications(recipient, type, message) VALUES (?,\"alert\",?);",
                        invite.sender, new Account(answer.getAccountId()).getUsername()+" has declined your invitation.");
            }
            //delete invite either way
            helper.executePreparedStatement("DELETE FROM notifications WHERE id = ?;", answer.inviteId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return match;
    }

    public static Match getMatchById(int matchId, int userId){
        try(DatabaseHelper helper = DatabaseHelper.create()){
            Match match = helper.executePreparedStatement("SELECT * FROM games WHERE id = ?;", (results ->{
                if(results.next())
                    return new Match(results);
                return null;
            }), matchId);
            if(match != null &&(match.getBlackId() == userId || match.getWhiteId() == userId))
                return match;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void insertMatchIntoDatabase(Match match, DatabaseHelper helper) throws SQLException {
        helper.executePreparedStatement("INSERT INTO games(id, board, white_player, black_player) VALUES (?,?,?,?);",
                match.getId(), match.getBoard().serializeToBytes(), match.getWhiteId(), match.getBlackId());
    }

    public static ArrayList<Match> getMatchByUserId(int userId){
        try(DatabaseHelper helper = DatabaseHelper.create()){
                helper.executePreparedStatement("SELECT * FROM games WHERE white_player, black_player = ?;", (results ->{
                ArrayList<Match> games = new ArrayList<>();
                while(results.next()) {
                   Match currMatch = new Match(results);
                   games.add(currMatch);
                }
                return games;
            }), userId);
           return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
