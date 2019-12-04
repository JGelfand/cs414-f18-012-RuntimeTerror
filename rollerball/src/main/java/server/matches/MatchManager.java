package server.matches;

import game.IllegalMoveException;
import server.accounts.Account;
import server.accounts.AccountManager;
import server.api.InviteAnswer;
import server.api.MoveRequest;
import server.api.MoveResponse;
import server.notifications.Notification;
import server.utils.DatabaseHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Map<String, Object>> getMatchesByUserId(int userId, boolean finishedGames){
        ArrayList<Map<String, Object>> games = new ArrayList<>();
        try(DatabaseHelper helper = DatabaseHelper.create()){
            helper.executePreparedStatement("SELECT * FROM games WHERE finished = ? AND (white_player = ? OR black_player = ?);", (results ->{
                while(results.next()) {
                    int opponentId;
                   if(!results.getString("white_player").equals(Integer.toString(userId))){
                       opponentId = results.getInt("white_player");
                   }else{
                       opponentId = results.getInt("black_player");
                   }
                   String username = new Account(opponentId).getUsername();
                   HashMap<String, Object> matchData = new HashMap<>();
                   matchData.put("id", results.getInt("id"));
                   matchData.put("opponentUsername", username);
                   games.add(matchData);
                }

                return games;
            }), finishedGames, userId, userId);
           return games;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
    }

    public static MoveResponse makeMove(MoveRequest moveRequest)  {
        MoveResponse response = new MoveResponse();
        response.success = true;
        Match target = getMatchById(moveRequest.matchId, moveRequest.getAccountId());
        try{
            response = target.move(moveRequest);
            target.saveToDB();
        } catch (SQLException | IOException e) {
            response.success = false;
            response.message = e.getMessage();
        }
        return response;
    }
}
