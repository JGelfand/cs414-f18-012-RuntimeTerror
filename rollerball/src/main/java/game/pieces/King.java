package game.pieces;

import game.Board;

public class King extends GamePiece {
    public King(int x, int y, boolean team){
        super(x,y,team);
    }

    @Override
    public boolean moveIsLegal(int toX, int toY, Board board) {
        boolean moveIsNextToKingAndOnBoard =  Math.abs(x-toX)<=1 && Math.abs(y-toY)<=1 &&
                locationIsOnBoard(toX, toY);
        if(!moveIsNextToKingAndOnBoard)
            return false;

        GamePiece destinationPiece = GamePiece.create(toX, toY, board);
        boolean destinationIsNotSameTeam =  destinationPiece != null? destinationPiece.team != team: true;
        if(!destinationIsNotSameTeam)
            return false;

        //TODO: Check that you don't end up in check

        return true;
    }

}
