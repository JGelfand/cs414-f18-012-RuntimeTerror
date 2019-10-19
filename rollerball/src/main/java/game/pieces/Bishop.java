package game.pieces;

import game.Board;

public class Bishop extends GamePiece {
    public Bishop(int x, int y, boolean team) {
        super(x, y, team);
    }

    @Override
    public boolean moveIsLegal(int toX, int toY, Board board) {
        if(!locationIsOnBoard(toX, toY))
            return false;

        GamePiece destinationPiece = GamePiece.create(toX, toY, board);
        if(destinationPiece != null && destinationPiece.team == team)
            return false;


        //TODO: actual move logic
        //TODO: ensure end state doesn't put your king in check
        return true;
    }
}
