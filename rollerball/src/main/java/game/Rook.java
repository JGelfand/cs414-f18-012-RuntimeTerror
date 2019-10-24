package game;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {
    private static final int[][] directions = new int[][]{new int[]{1,0},new int[]{0,1},new int[]{-1,0},new int[]{0,-1}};
    public Rook(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public ArrayList<String> legalMoves() {
        ArrayList<String> moves = new ArrayList<>();
        for(int[] direction: directions){
            addMovesByDirection(moves, direction);
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
