package server.api;

public class AuthenticatedRequest {
    private  AuthenticationToken token;
    public boolean verify(){
        return token.verify();
    }
}
