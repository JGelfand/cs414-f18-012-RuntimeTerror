package game.pieces;

import game.Board;

public class Pawn extends GamePiece {
    public Pawn(int x, int y, boolean team){
        super(x,y,team);
    }

    @Override
    public boolean moveIsLegal(int toX, int toY, Board board) {
        if (Math.abs(x-toX) > 1 || Math.abs(y-toY) > 1) //similar to a king, only move one square in every direction
	{
		return false;
	}
	if(moveIsBackward(int toX, int toY)) //cant move backwards
	{
		return false;
	}
	if (moveIsSidwise(int toX, int toY))
	{
		return false;
	}
	if (toX == x && toY == y) //cant not change position
	{
		return false;
	}

        //TODO: Check that you don't end up in check

        return true;
    }

}
