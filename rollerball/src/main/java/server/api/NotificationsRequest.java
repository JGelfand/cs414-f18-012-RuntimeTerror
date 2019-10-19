package server.api;

public class NotificationsRequest extends AuthenticatedRequest {
    public int getAccountId(){
        return token.getAccountId();
    }
}
