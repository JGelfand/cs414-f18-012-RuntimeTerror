package server.api;

import org.bouncycastle.util.encoders.Base64;
import server.accounts.Account;
import server.utils.SecureHashing;

import java.security.SecureRandom;

public class AuthenticationToken {
    private long time;
    private String user;
    private static final int secret = new SecureRandom().nextInt();
    private String signature;
    private int id;

    @Override
    public String toString(){
        return String.format("{\"time\": %d, \"user\": %s, \"id\": %d, \"signature\":, %s}", time, user, id, signature);
    }

    public static AuthenticationToken generateToken(Account account){
        AuthenticationToken newToken = new AuthenticationToken();
        newToken.user = account.getUsername();
        newToken.id = account.getAccountId();
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
        return Base64.toBase64String(SecureHashing.hash(id+","+user+","+time+","+secret));
    }

    public boolean verify(){
        boolean signatureValid = generateSignature().equals(signature);
        boolean timeOK = System.currentTimeMillis() >= time && System.currentTimeMillis() <= time + (24 * 60 * 60 * 1000);
        return  signatureValid && timeOK;
    }

    public String getUsername(){return user;}
    public int getAccountId(){return id;}
}
