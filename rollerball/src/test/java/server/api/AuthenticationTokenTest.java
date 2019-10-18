package server.api;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthenticationTokenTest {

    @Test
    public void verify() {
        //try sending a token through Gson to make sure it works
        AuthenticationToken validToken = AuthenticationToken.createFakeToken("someone", System.currentTimeMillis());
        Assert.assertTrue("Token was generated with good time. Should be valid.", validToken.verify());
        Gson gson = new Gson();
        String serializedValidToken = gson.toJson(validToken);
        validToken = gson.fromJson(serializedValidToken, AuthenticationToken.class);
        Assert.assertTrue("Token was generated with good time. Should still work after serialization.", validToken.verify());

        //now create an invalid token (time of more than one day ago) and do the same process
        AuthenticationToken invalidToken = AuthenticationToken.createFakeToken("someoneElse", System.currentTimeMillis() - 2*24*60*60*1000);
        Assert.assertFalse("Token thinks it was generated two days ago. Shouldn't work.", invalidToken.verify());
        String serializedInvalidToken = gson.toJson(invalidToken);
        invalidToken = gson.fromJson(serializedInvalidToken, AuthenticationToken.class);
        Assert.assertFalse("Token was invalid before, should be invalid after.", invalidToken.verify());
    }
}