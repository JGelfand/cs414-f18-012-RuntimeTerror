package server.accounts;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;

import org.bouncycastle.util.encoders.Base64;
import server.api.AuthenticationToken;
import server.api.LoginRequest;
import server.api.RegistrationRequest;
import server.api.RegistrationOrLoginResponse;
import server.utils.DatabaseHelper;
import server.utils.SecureHashing;

public class AccountManager {
	private static final SecureRandom random = new SecureRandom();
	public static RegistrationOrLoginResponse registerUser(RegistrationRequest request){
		return registerUser(request, false);
	}
	public static RegistrationOrLoginResponse registerUser(RegistrationRequest request, boolean testing){
		RegistrationOrLoginResponse response = new RegistrationOrLoginResponse();
		response.errorMessage = "";
		response.success = true;
		
		if(request.password == null || request.password.length() < 8){
			response.success = false;
			response.errorMessage += "Password must be at least 8 characters.\n";
		}
		
		if(request.email == null || !request.email.matches("[a-zA-Z1-9.]+@([a-zA-Z1-9.]+\\.[a-zA-Z0-9]+)+")){
			response.success = false;
			response.errorMessage+="Invalid email.\n";
		}
		
		if(request.username == null ||request.username.equals("admin")||!request.username.matches("[a-zA-Z0-9]+")){
			response.success = false;
			response.errorMessage+="Invalid username.\n";
		}

		if(response.success && !testing){ //if stuff is valid, check to make sure there are no existing usernames/emails that match in the records
			try(DatabaseHelper helper = DatabaseHelper.create()){
				boolean emailCollision = helper.executePreparedStatement("SELECT COUNT(*) from users where email = ? ;", (results -> {
					if(results.next()){
						return results.getInt("COUNT(*)") != 0;
					}
					return false;
				}), request.email);
				if(emailCollision) {
					response.success = false;
					response.errorMessage += "Email already in use.\n";
				}
				boolean usernameCollision = helper.executePreparedStatement("SELECT COUNT(*) from users where username = ? ;", (results -> {
					if(results.next()){
						return results.getInt("COUNT(*)") != 0;
					}
					return false;
				}), request.username);
				if(usernameCollision){
					response.success = false;
					response.errorMessage += "Username already in use.\n";
				}

				//if successful, add to table
				if(response.success){
					addUserToDatabase(helper, request);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				response.success = false;
			} catch (IOException e) {
				e.printStackTrace();
				response.success = false;
			}


		}

		if(response.success){
			response.token = AuthenticationToken.generateToken(request.username);
		}
		return response;
	}

	private static void addUserToDatabase(DatabaseHelper helper, RegistrationRequest request) throws SQLException{
		int salt;
		synchronized (random){
			salt = random.nextInt();
		}
		byte[] hash = SecureHashing.hash(request.password+salt);
		String hashString = Base64.toBase64String(hash);
		helper.executePreparedStatement("INSERT INTO users (username, email, salt, hash) VALUES (?, ?, ?, ?) ;",request.username, request.email, salt, hashString);
	}

	public static RegistrationOrLoginResponse loginUser(LoginRequest request){
		RegistrationOrLoginResponse response = new RegistrationOrLoginResponse();
		response.success = true;
		response.errorMessage = "";
		try(DatabaseHelper helper = DatabaseHelper.create()) {
			Account account = helper.executePreparedStatement("SELECT * FROM users WHERE username = ? ;", (results) -> {
				if (results.next()) {
					return new Account(results);
				}
				return null;
			}, request.username);
			if(account == null){
				response.success = false;
				response.errorMessage = "Wrong username";
				return response;
			}
			String newHash = Base64.toBase64String(SecureHashing.hash(request.password + account.getSalt()));
			if (newHash.equals(account.getHash()))
				response.token = AuthenticationToken.generateToken(request.username);

			else {
				response.errorMessage += "Wrong password.";
				response.success = false;
			}
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.success = false;
		}catch (SQLException e){
			e.printStackTrace();
			response.success = false;
		}
		return response;
	}
}
