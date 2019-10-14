package server.accounts;

import server.utils.DatabaseHelper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private int accountId;
    private String username;
    private String email;
    private String hash;
    private Integer salt;

    public Account(int accountId){
        this.accountId = accountId;
    }

    public Account(ResultSet resultSet) throws SQLException{
        addAll.processResults(resultSet);
    }

    private DatabaseHelper.ResultProcessor<Boolean> addAll = (ResultSet results) -> {
        accountId = results.getInt("id");
        username = results.getString("username");
        email = results.getString("email");
        hash = results.getString("hash");
        salt = results.getInt("salt");
        return true;
    };

    private  static DatabaseHelper.ResultProcessor<String> getStringFieldFromDatabase(String fieldName){
        return (ResultSet results) -> {
            results.next();
            return results.getString("fieldName");
        };
    }

    private static DatabaseHelper.ResultProcessor<Integer> getSaltFromDatabase = (ResultSet results) -> {results.next(); return results.getInt("salt");};

    public int getAccountId() {
        return accountId;
    }

    public String getUsername(){
        if(username == null){
            try (DatabaseHelper helper = DatabaseHelper.create()){
                username = helper.executeStatement("SELECT username FROM users WHERE id = "+accountId+" ;", getStringFieldFromDatabase("username"));
            }catch (SQLException e){
                return null;
            }catch (IOException e){
                return null;
            }
        }
        return username;
    }

    public String getHash(){
        if(hash == null){
            try (DatabaseHelper helper = DatabaseHelper.create()){
                hash = helper.executeStatement("SELECT hash FROM users WHERE id = "+accountId+" ;", getStringFieldFromDatabase("hash"));
            }catch (SQLException e){
                return null;
            }catch (IOException e){
                return null;
            }
        }
        return hash;
    }

    public String getEmail(){
        if(email == null){
            try (DatabaseHelper helper = DatabaseHelper.create()){
                email = helper.executeStatement("SELECT hash FROM users WHERE id = "+accountId+" ;", getStringFieldFromDatabase("email"));
            }catch (SQLException e){
                return null;
            }catch (IOException e){
                return null;
            }
        }
        return email;
    }

    public Integer getSalt(){
        if(salt == null){
            try (DatabaseHelper helper = DatabaseHelper.create()){
                salt = helper.executeStatement("SELECT salt FROM users WHERE id = "+accountId+" ;", getSaltFromDatabase);
            }catch (SQLException e){
                return null;
            }catch (IOException e){
                return null;
            }
        }
        return salt;
    }
}
