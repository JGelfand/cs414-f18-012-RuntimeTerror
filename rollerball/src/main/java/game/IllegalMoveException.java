package game;

public class IllegalMoveException extends Exception {
    public IllegalMoveException(String s) {
        super(s);
    }

    public IllegalMoveException(Exception e) {
        super(e);
    }
}
