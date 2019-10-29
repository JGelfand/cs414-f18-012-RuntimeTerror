package game;

import java.util.ArrayList;

public class Bishop extends ChessPiece{
    private static final int[][] forwardDirections = new int[][]{new int[]{1,1},new int[]{-1,1}};
    private static final int[][] backwardDirections = new int[][]{new int[]{-1,-1},new int[]{1,-1}};
    public Bishop(ChessBoard board, ChessPiece.Color color) {
        super(board, color);
    }

    @Override
    public ArrayList<String> legalMoves() {
        ArrayList<String> moves = new ArrayList<>();
        for(int[] direction: forwardDirections){
            addMovesByDirection(moves, direction);
        }
        for(int[] direction: backwardDirections){
            addOneMoveByDirection(moves, direction);
        }
        return moves;
    }

    @Override
    public String toString() {
        if(color == Color.WHITE){
            return "\u2657";
        }
        return "\u265D";
    }
}
