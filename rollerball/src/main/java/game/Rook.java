package game;

import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends ChessPiece {
    private static final int[] backwards = new int[]{0,-1}, forwards = new int[]{0,1};
    private static final int[][] otherDirections = new int[][]{new int[]{1,0},new int[]{-1,0},forwards};
    public Rook(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public ArrayList<String> legalMoves() {
        //if backwards for us, always go back one. If forwards, always do the whole line+bounce. If sideways:
        //go forward one space and check if our direction is now 'forward' in this position
        //if it is, continue move in a line from that position
        //if it isn't, break
        //note: forwards can safely use the sideways case
        ArrayList<String> moves = new ArrayList<>();
        addOneMoveByDirection(moves, backwards);
        for(int[] direction:otherDirections){
            int[] orientedDirection = getOrientedDirection(getPosition(), direction);
            String newPosition = changePosition(getPosition(), orientedDirection);
            if(newPosition == null)
                continue;
            addMovesByDirection(moves, orientedDirection, getPosition(), true);
        }
        return moves;
    }



    @Override
    public String toString() {
        if(color == Color.WHITE)
            return "\u2656";
        return "\u265C";
    }
}
