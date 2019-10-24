package game;


import java.util.ArrayList;
import java.util.List;

public abstract class ChessPiece {
    protected ChessBoard board;
    protected Color color;
    protected int row;
    protected int column;
    private String position = "a1";

    public String getPosition() {
        return position;
    }

    public enum Color{WHITE, BLACK}

    public ChessPiece(ChessBoard board, Color color){
        this.board = board;
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public  void setPosition(String position) throws IllegalPositionException{
        if(positionIsValid(position)) {
            this.position = position;
            this.row = position.charAt(1)-'1';
            this.column = position.charAt(0) - 'a';
        }
        else throw new IllegalPositionException("Bad position: "+position+".");
    }

    protected boolean positionIsValid(String position){
        return position.matches("\\A[a-h][1-8]\\z");
    }

    abstract public ArrayList<String> legalMoves();

    abstract public String toString();

    protected void addMovesByDirection(List<String> validMoves, int[] direction){
        String position = changePosition(getPosition(), direction);
        try {
            while (position != null) {
                ChessPiece piece = board.getPiece(position);
                if(piece != null){
                    if(piece.color != color)
                        validMoves.add(position);
                    break;
                }
                validMoves.add(position);
                position = changePosition(position, direction);
            }
        } catch (IllegalPositionException e) {
            //should never reach here. Damn you checked exceptions.
            throw new RuntimeException(e);
        }
    }

    //direction vectors are in x,y format
    protected String changePosition(String position, int[] direction){
        String newPosition = (char)(position.charAt(0)+direction[0])+""+(char)(position.charAt(1)+direction[1]);
        if(positionIsValid(newPosition))
            return newPosition;
        else
            return null;
    }
}
