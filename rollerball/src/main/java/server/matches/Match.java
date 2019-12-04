package server.matches;

import com.google.gson.*;
import game.ChessBoard;
import game.ChessPiece;
import game.King;
import game.IllegalMoveException;
import game.IllegalPositionException;
import server.utils.DatabaseHelper;
import server.api.MoveResponse;
import server.api.MoveRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private boolean whiteKCValid;
    private boolean blackKCValid;

    public static Match createNewMatch(int id, int whiteId, int blackId){
        Match match = new Match();
        match.board = new ChessBoard();
        match.board.initialize();
        match.id = id;
        match.whiteId = whiteId;
        match.blackId = blackId;
        match.turn = true;
	match.whiteKCValid = false;
	match.blackKCValid = false;
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
	this.whiteKCValid = results.getBoolean("white_circle");
	this.blackKCValid = results.getBoolean("black_circle");
    }

    private void updateFlag(ChessPiece movingPiece, String pos)
    {
	if (movingPiece instanceof King)
	{
	    if (movingPiece.getColor() == ChessPiece.Color.WHITE)
	    {
		if (pos.equals("a3") || pos.equals("b3"))
		{
		    whiteKCValid = true;
		}
		else if (pos.equals("f3") || pos.equals("g3"))
		{
		    whiteKCValid = false;
		}
	    }
	    else //moving piece is black
	    {
		if (pos.equals("f5") || pos.equals("g5"))
		{
		    blackKCValid = true;
		}
		else if (pos.equals("a5") || pos.equals("b5"))
		{
		    blackKCValid = false;
		}
	    }
	}
	return;
    }

    public MoveResponse move(MoveRequest moveRequest)
    {
	MoveResponse response = new MoveResponse();
	if (moveRequest.forfeit == true) //catch if you gave up
	{
		response.success = true;
		response.message = "Never gonna give up";
		response.gameOver = moveRequest.getAccountId() == blackId ? "BLACK" : "WHITE"; //mover lost as they resigned
		return response;
	}
	else if ((moveRequest.getAccountId() == whiteId && !turn) || (moveRequest.getAccountId() == blackId && turn)) //catch if moving on not your turn
	{
	    response.success = false;
	    response.message = "It is not your turn";
	    return response;
        }
	try{ //catch if the player is trying to move his opponents piece
	    ChessPiece moving = board.getPiece(moveRequest.from);
	    if ( moving == null || (moving.getColor() == ChessPiece.Color.WHITE && moveRequest.getAccountId() == blackId) ||
				   (moving.getColor() == ChessPiece.Color.BLACK && moveRequest.getAccountId() == whiteId) ) 
	    {
		response.success = false;
		response.message = moving ==null? "Start position needs to be a piece.": "Can't move your opponent's piece.";
		return response;
	    }
	}
	catch (IllegalPositionException e)
	{
	    response.success = false;
	    response.message = e.getMessage();
	}

	try {
	    
	    ChessPiece mover = this.board.move(moveRequest.from, moveRequest.to, moveRequest.promoteTo); //make the move
	    response.success = true; //didnt error, so made the move
	    response.message = "";
	    updateFlag(mover, moveRequest.to); //update the flag

	    turn = !turn; //alternate turns

	    if (game_is_over(mover, moveRequest.to))
	    {
		if (game_is_won(mover, moveRequest.to))
		{
		    response.gameOver = mover.getColor() == ChessPiece.Color.WHITE ? "WHITE" : "BLACK"; //winner moved 
		}
		else //game_is_drawn()
		{
		    response.gameOver = "DRAW";
		}
	    }
	    else
	    {
		response.gameOver = "";
	    }
	}
	catch (IllegalMoveException e) {
	    response.success = false;
	    response.message = e.getMessage();
	}
	return response;
    }
    
    public boolean game_is_over(ChessPiece piece, String pos)
    {
	if (game_is_won(piece, pos))
	{
	    return true;
	}
	else if (game_is_draw(piece))
	{
	    return true;
	}
	return false; //the game can never end Chell...
    }

    private boolean game_is_mate(ChessPiece piece)
    {
	if(game_is_stalemate(piece)) //opposing team cant move
	{
	    ChessPiece.Color losing_color = piece.getColor() == ChessPiece.Color.WHITE ? ChessPiece.Color.BLACK : ChessPiece.Color.WHITE;
	    if (board.king_in_check(losing_color, board.getKingLocation(losing_color)))
	    {
		return true; //no legal moves, and king is in check, therefore checkmate
	    }
	}
	return false;
    }

    private boolean game_is_circle(ChessPiece piece, String pos)
    {
	if (piece instanceof King)
	{
	    if (piece.getColor() == ChessPiece.Color.WHITE)
	    {
		if (pos.equals("d6") && whiteKCValid)
		{
		    return true;
		}
	    }
	    else
	    {
		if (pos.equals("d2") && blackKCValid)
		{
		    return true;
		}
	    }
	}
	return false;
    }

    //returns true if the player opposite piece is in checkmate, false otherwise
    public boolean game_is_won(ChessPiece piece, String pos)
    {
	return game_is_mate(piece) || game_is_circle(piece, pos);
    }

    public boolean game_is_draw(ChessPiece piece)
    {
	return game_is_stalemate(piece); //for now, lets only support stalemate
	//if we want we could check for three fold repitition, but that sounds difficult
    }

    private boolean game_is_stalemate(ChessPiece piece)
    {
	ChessPiece[][] b = board.getBoard();
	for (int k = 0; k < b.length; k++)
	{
	    for (int j = 0; j < b[k].length; j++)
	    {
		ChessPiece mover = b[k][j];
		if (mover != null && mover.getColor() != piece.getColor()) //mover is on the enemy team team
		{
		    ArrayList<String> moves = mover.legalMoves();
		    for (String move : moves)
		    {
			if (!board.king_in_check(mover, move))
			{
			    return false; //the enemy team has at least this move
			}
		    }
		}
	    }
	}
	return true; //no moves were found for the next turn, therefore it is stalemate. 
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

    public void saveToDB() throws SQLException, IOException {
		try (DatabaseHelper helper = DatabaseHelper.create()) {
			helper.executePreparedStatement("UPDATE games SET board = ?, turn = ?, white_circle = ?, black_circle = ?, white_forfeit = ?, black_forfeit = ?   WHERE id = ?;",
					getBoard().serializeToBytes(), turn, whiteKCValid, blackKCValid, whiteForfeit, blackForfeit, getId());
		}
	}
}
