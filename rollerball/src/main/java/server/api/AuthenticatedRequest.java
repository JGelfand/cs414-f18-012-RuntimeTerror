package server.api;

public class AuthenticatedRequest {
    protected  AuthenticationToken token;
    public int getAccountId(){
        return token.getAccountId();
    }
    public boolean verify(){
        return token.verify();
    }
}
