package game;


import java.util.*;

public abstract class ChessPiece {
    protected ChessBoard board;
    protected Color color;
    protected int row;
    protected int column;
    private String position = "a1";
    private static final Set<String> corners = new HashSet<>(Arrays.asList("a1", "a7", "g1", "g7"));

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
        return position != null && position.matches("\\A[a-g][1-7]\\z") &&
                (position.charAt(0) <'c' || position.charAt(0) > 'e' ||
                position.charAt(1) < '3' || position.charAt(1) > '5');
    }

    abstract public ArrayList<String> legalMoves();

    abstract public String toString();

    protected void addMovesByDirection(List<String> validMoves, int[] direction){
        direction = getOrientedDirection(getPosition(), direction);
        addMovesByDirection(validMoves, direction, getPosition(), true);
    }

    protected void addMovesByDirection(List<String> validMoves, int[] direction, String initialPosition, boolean canBounce){
        String position = changePosition(initialPosition, direction);
        try {
            while (position != null) {
                ChessPiece piece = board.getPiece(position);
                if(piece != null){
                    if(piece.color != color)
                        validMoves.add(position);
                    break;
                }
                validMoves.add(position);
                String newPosition = changePosition(position, direction);
                if(newPosition == null){
                    //check for bounces
                    boolean corner = corners.contains(position);
                    //if corner:  rotate by -90 degrees
                    if(corner){
                        direction = getOrientedDirection("a7", direction);//position is in the top, which orients from up to right, ie -90 degrees
                    }
                    //if edge: reflect direction vector vertically or horizontally
                    else {
                        //try deflecting vertically. if that fails, try reflecting horizontally
                        int[] newDirection = new int[]{direction[0], direction[1]};
                        newDirection[1]*=1;
                        if(!positionIsValid(changePosition(position, newDirection))){
                            newDirection[0]*=-1;
                            newDirection[1]*=-1;
                            direction = newDirection;
                        }
                    }
                    newPosition = changePosition(position, direction);
                    if(canBounce &&newPosition != null && !newPosition.equals(position))
                        addMovesByDirection(validMoves, direction, position, false);
                    break;
                }
                position = newPosition;
                int[] backwards = getOrientedDirection(position, new int[]{0,-1});
                if(backwards[0]*direction[0]+backwards[1]*direction[1] > 0)
                    break;
            }
        } catch (IllegalPositionException e) {
            //should never reach here. Damn you checked exceptions.
            throw new RuntimeException(e);
        }
    }

    protected void addOneMoveByDirection(List<String> validMoves, int[] direction){
        direction = getOrientedDirection(getPosition(), direction);
        String position = changePosition(getPosition(), direction);
        if(position == null)
            return;
        try {
            ChessPiece piece = board.getPiece(position);
            if(piece == null || piece.getColor() != getColor())
                validMoves.add(position);

        }catch (IllegalPositionException e){
            throw new RuntimeException(e);
        }
    }

    protected int[] getOrientedDirection(String position, int[] initialDirection){
        if(!positionIsValid(position))
            return null;
        int[][] rotationMatrix;
        //determine matrix based on board location
        //default orientation is "up", for which the rotation matrix is the identity
        int x = position.charAt(0)-'a'+1;
        int y = position.charAt(1)-'1'+1;
        if((x == 1 && y >=1 && y <=6) ||
                (x==2 && y>=2 && y<=5)){
            rotationMatrix = new int[][]{new int[]{1,0}, new int[]{0,1}};
        }
        else if((x == 6 && y>=3 && y<=6) ||
                (x == 7 && y >= 2 && y <= 7)){
            rotationMatrix = new int[][]{new int[]{-1,0}, new int[]{0,-1}};
        }
        else if ((y == 1 && x>=2 && x<=7) ||
                (y == 2 && x >=3 && x<=6 ) ){
            rotationMatrix = new int[][]{new int[]{0,-1}, new int[]{1,0}};
        }
        else {
            rotationMatrix = new int[][]{new int[]{0,1}, new int[]{-1,0}};
        }

        return new int[]{
                rotationMatrix[0][0] * initialDirection[0] + rotationMatrix[0][1] * initialDirection[1],
                rotationMatrix[1][0] * initialDirection[0] + rotationMatrix[1][1] * initialDirection[1],
        };
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
