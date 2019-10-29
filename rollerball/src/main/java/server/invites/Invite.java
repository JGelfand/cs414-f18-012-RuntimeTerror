package server.invites;
import server.utils.DatabaseHelper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Invite{

    private int sender;
    private int recipient;
    private String message;
    private String type;


    public Invite(int sender, int recipient, String message, String type){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.type = type;
    }

    public Invite(ResultSet resultSet) throws SQLException {
        addAll.processResults(resultSet);
    }

    private DatabaseHelper.ResultProcessor<Boolean> addAll = (ResultSet results) -> {
        sender = results.getInt("sender");
        message = results.getString("message");
        type = results.getString("type");
        recipient = results.getInt("recipient");
        return true;
    };

    public static List<Invite> getPendingInvites(int accoundID){

        ArrayList<Invite> results = new ArrayList<>();
        try (DatabaseHelper helper = DatabaseHelper.create()){
            helper.executePreparedStatement("SELECT * FROM invites WHERE recipient = ? ;", (ResultSet tableResults) -> {
                while (tableResults.next()) {
                    Invite currInvite = new Invite(tableResults);
                    results.add(currInvite);
                }return null;
            }, accoundID);
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

}