package game;

public class Game {
    private Board board;
    private char[][] getBoard(){
        return board.getBoard();
    }

    public enum Direction{
        UP, DOWN, LEFT, RIGHT
    }


}
