package server.matches;

public class MoveResponse {
    public boolean success;
    public String gameOver; //"" or null if not, "WHITE" or "DRAW" or "BLACK" if yes "RESIGN" is also supported
    public String errorMessage;
}
