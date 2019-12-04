package server.api;

import server.api.AuthenticatedRequest;

public class MoveRequest extends AuthenticatedRequest {
    public String from, to;
    public String promoteTo;
    public boolean forfeit;
    public int matchId;

    public void setFakeToken(AuthenticationToken token)
    {
	this.token = token;
    }
}
