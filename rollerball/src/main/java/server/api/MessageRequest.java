package server.api;

public class MessageRequest extends AuthenticatedRequest{
    public String recipient;
    public String message;
    public String type;
}
