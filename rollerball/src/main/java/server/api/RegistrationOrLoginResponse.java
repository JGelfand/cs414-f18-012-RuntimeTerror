package server.api;

public class RegistrationOrLoginResponse {
    public boolean success;
    public String errorMessage;
    public AuthenticationToken token; //should be null if success is false
}
