package server.matches;

import com.google.gson.*;
import game.ChessBoard;
import server.utils.DatabaseHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
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
    public static Match createNewMatch(int id, int whiteId, int blackId){
        Match match = new Match();
        match.board = new ChessBoard();
        match.board.initialize();
        match.id = id;
        match.whiteId = whiteId;
        match.blackId = blackId;
        match.turn = true;
        return match;
    }
    private Match(){}

    public Match(ResultSet results) throws SQLException {
        this.board = new ChessBoard(results.getBytes("board"));
        this.id = results.getInt("id");
        this.whiteId = results.getInt("white_player");
        this.blackId = results.getInt("black_player");
        this.turn = results.getBoolean("turn");
        this.whiteForfeit = results.getBoolean("white_forfeit");
        this.blackForfeit = results.getBoolean("black_forfeit");
    }
    public ChessBoard getBoard(){
        return board;
    }

    public int getId(){
        return id;
    }

    public int getWhiteId(){
        return whiteId;
    }

    public int getBlackId(){
        return blackId;
    }
}
