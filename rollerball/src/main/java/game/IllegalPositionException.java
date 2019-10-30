package game;

public class IllegalPositionException extends Exception {
    public IllegalPositionException(){}
    public IllegalPositionException(String s) {
        super(s);
    }
    public IllegalPositionException(Exception e){
        super(e);
    }
}
