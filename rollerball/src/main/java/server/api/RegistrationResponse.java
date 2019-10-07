package server.api;

public class RegistrationResponse {
    public boolean success;
    public String errorMessage;
    public AuthenticationToken token; //should be null if success is false
}
