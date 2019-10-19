package server.api;

public class AuthenticatedRequest {
    protected  AuthenticationToken token;
    public boolean verify(){
        return token.verify();
    }
}
