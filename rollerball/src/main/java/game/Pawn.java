package game;

import java.util.ArrayList;

public class Pawn extends ChessPiece {
    private final int[] direction;
    public Pawn(ChessBoard board, Color color) {
        super(board, color);
        direction = this.color == Color.WHITE? new int[]{0,1}:new int[]{0,-1};
    }

    @Override
    public ArrayList<String> legalMoves() {
        ArrayList<String> moves = new ArrayList<>();

        String position = getPosition();

        String move = changePosition(position, direction);
        try{
            if(board.getPiece(move) == null)
                moves.add(move);
        } catch (IllegalPositionException e) {
            //an exception here means we're on the back row and can't move forward at all. Nothing will work.
            return moves;
        }

        for(int[] horizontal:new int[][]{new int[]{-1,0},new int[]{1,0}}){
            String captureMove = changePosition(move, horizontal);
            try {
                ChessPiece capturePiece = board.getPiece(captureMove);
                if(capturePiece != null && capturePiece.getColor() != color){
                    moves.add(captureMove);
                }
            } catch (IllegalPositionException e) {
                continue;
            }
        }

        //Handle double move forward. Detect if you're in the 'initial position' by row
        char startRow = color == Color.WHITE?'2':'7';
        if(position.charAt(1) == startRow && moves.contains(move)){
            try{
                String doubleMove = changePosition(move, direction);
                if(board.getPiece(doubleMove) == null)
                    moves.add(doubleMove);
            } catch (IllegalPositionException e) {
                //Just don't add the move.
            }
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
