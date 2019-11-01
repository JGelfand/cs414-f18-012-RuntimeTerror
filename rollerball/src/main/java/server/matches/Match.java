package server.matches;

import com.google.gson.*;
import game.ChessBoard;
import server.utils.DatabaseHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;

public class Match {
    public static class MatchSerializer implements JsonSerializer<Match> {
        @Override
        public JsonElement serialize(Match src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject base = new JsonObject();
            base.add("turn", new JsonPrimitive(src.turn));
            base.add("whiteForfeit", new JsonPrimitive(src.whiteForfeit));
            base.add("blackForfeit", new JsonPrimitive(src.blackForfeit));
            base.add("id", new JsonPrimitive(src.id));
            base.add("whiteId", new JsonPrimitive(src.whiteId));
            base.add("blackId", new JsonPrimitive(src.blackId));
            base.add("board", new JsonPrimitive(src.board.toString()));
            return base;
        }
    }
    private transient ChessBoard board;
    private boolean turn;
    private boolean whiteForfeit, blackForfeit;
    private int id;
    private int whiteId, blackId;
    public Match createMatch(int id, int whiteId, int blackId){
        Match match = new Match();
        match.board = new ChessBoard();
        match.board.initialize();
        match.id = id;
        match.whiteId = whiteId;
        match.blackId = blackId;

        try(DatabaseHelper helper = DatabaseHelper.create()){
            helper.executePreparedStatement("INSERT INTO games(id, board, white_player, black_player) VALUES (?,?,?,?);", id, match.board.serializeToBytes(), whiteId, blackId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return match;
    }
    private Match(){}
}
