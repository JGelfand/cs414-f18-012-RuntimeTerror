package server.api;
import server.api.AuthenticatedRequest;

public class MatchesRequest extends AuthenticatedRequest{
    public boolean finishedGames;
    public int userID;

}