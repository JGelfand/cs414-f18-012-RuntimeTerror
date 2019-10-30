package game;

import java.util.ArrayList;

public class King extends ChessPiece{
    public King(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public ArrayList<String> legalMoves() {
        ArrayList<String> moves = new ArrayList<>();
        String position = getPosition();
        for(int horizontal = -1; horizontal<= 1; horizontal++){
            for(int vertical = -1; vertical <= 1; vertical++){
                try{
                    String move = changePosition(position, new int[]{horizontal, vertical});
                    ChessPiece piece = board.getPiece(move);
                    if(piece == null || piece.getColor() != this.getColor())
                        moves.add(move);
                }catch (IllegalPositionException e){
                    continue;
                }
            }
        }

        return moves;
    }



    @Override
    public String toString() {
        if(color == Color.WHITE)
            return "\u2654";
        return "\u265A";
    }
}
