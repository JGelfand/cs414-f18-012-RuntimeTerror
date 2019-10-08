package server.api;

import org.bouncycastle.util.encoders.Base64;
import server.utils.SecureHashing;

import java.security.SecureRandom;

public class AuthenticationToken {
    private long time;
    private String user;
    private static final int secret = new SecureRandom().nextInt();
    private String signature;

    @Override
    public String toString(){
        return String.format("{\"time\": %d, \"user\": %s, \"signature\":, %s}", time, user, signature);
    }

    public static AuthenticationToken generateToken(String user){
        AuthenticationToken newToken = new AuthenticationToken();
        newToken.user = user;
        newToken.time = System.currentTimeMillis();
        newToken.signature = newToken.generateSignature();
        return newToken;
    }

    public static AuthenticationToken createFakeToken(String user, long fakeTime){
        AuthenticationToken newToken = new AuthenticationToken();
        newToken.user = user;
        newToken.time = fakeTime;
        newToken.signature = newToken.generateSignature();
        return newToken;
    }

    private String generateSignature(){
        return Base64.toBase64String(SecureHashing.hash(user+","+time+","+secret));
    }

    public boolean verify(){
        boolean signatureValid = generateSignature().equals(signature);
        boolean timeOK = System.currentTimeMillis() >= time && System.currentTimeMillis() <= time + (24 * 60 * 60 * 1000);
        return  signatureValid && timeOK;
    }
}
