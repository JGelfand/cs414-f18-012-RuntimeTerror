package server;

import org.junit.Assert;
import org.junit.Test;

import server.accounts.AccountManager;
import server.api.RegistrationRequest;

public class AccountManagerTest {
	@Test
	public void registerUser(){
		RegistrationRequest request = new RegistrationRequest();
		request.username = "abcdefg";
		request.email = "a@b.com";
		Assert.assertFalse("Nonexistent password should fail", AccountManager.registerUser(request).success);
		
		request.password = "short";
		Assert.assertFalse("Short password should fail", AccountManager.registerUser(request).success);

		request.password = "Long Enough";
		request.username = "a@b.com";
		Assert.assertFalse("Username that is email should fail", AccountManager.registerUser(request).success);
		
		request.username = "b.com";
		Assert.assertFalse("Username that is url should fail", AccountManager.registerUser(request).success);

		request.username="validUsername001";
		request.email="notAnEmail";
		Assert.assertFalse("Invalid email should fail", AccountManager.registerUser(request).success);
		
		request.email = "valid.Email@mail.something.com";
		Assert.assertTrue("Everything valid should pass", AccountManager.registerUser(request, true).success);

	}
}
