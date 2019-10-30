package game;

import java.util.ArrayList;

public class Pawn extends ChessPiece {
    private final int[][] directions = new int[][]{new int[]{-1, 1}, new int[]{0, 1}, new int[]{1, 1}};
    public Pawn(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public ArrayList<String> legalMoves() {
        ArrayList<String> moves =  new ArrayList<>();
        for(int[] direction:directions){
            addOneMoveByDirection(moves, direction);
        }
        return moves;
    }

    @Override
    public String toString() {
        if(color == Color.WHITE)
            return "\u2659";
        return "\u265F";
    }
}
