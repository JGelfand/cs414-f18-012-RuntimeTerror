package server.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import com.mysql.jdbc.Driver;

public class DatabaseHelper implements Closeable {
    public interface ResultProcessor<T>{
        public T processResults(ResultSet results) throws SQLException;
    }
    private static String username, password;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner loginFile = new Scanner(DatabaseHelper.class.getClassLoader().getResourceAsStream("sql_login_info.txt"));
        username = loginFile.next();
        password = loginFile.next();
        loginFile.close();
    }
    private Connection connection;

    public static DatabaseHelper create() throws SQLException {
        return new DatabaseHelper(username, password);
    }
    private DatabaseHelper(String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://faure.cs.colostate.edu/runtimeterror", user, password);
    }

    public <T> T executeStatement(String statementString, ResultProcessor<T> processor) throws SQLException{
        try(Statement statement = connection.createStatement()){
            ResultSet results = statement.executeQuery(statementString);
            return processor.processResults(results);
        }
    }

    public void executeStatement(String statementString) throws SQLException{
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(statementString);
        }
    }

    public void executePreparedStatement(String statementString, Object ... args) throws SQLException{
        try(PreparedStatement statement = connection.prepareStatement(statementString)){
            for(int i=0; i< args.length; i++){
                setData(statement, args[i], i+1);
            }
            statement.executeUpdate();
        }
    }

    public <T> T executePreparedStatement(String statementString, ResultProcessor<T> processor, Object ... args) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(statementString)){
            for(int i=0; i< args.length; i++){
                setData(statement, args[i], i+1);
            }
            ResultSet results = statement.executeQuery();
            return processor.processResults(results);
        }
    }

    private void setData(PreparedStatement statement, Object data, int pos) throws SQLException {
        if(data instanceof String){
            statement.setString(pos, (String) data);
        }
        else if(data instanceof Integer){
            statement.setInt(pos, (Integer) data);
        }
        else if (data instanceof byte[]){
            statement.setBytes(pos, (byte[]) data);
        }
        else if (data instanceof Boolean){
            statement.setBoolean(pos, (Boolean) data);
        }
        else throw new IllegalArgumentException("setData doesn't currently support arguments of type"+data.getClass().getName());
    }
    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        if(args.length != 2){
            System.out.println("Args: <username> <password>");
        }
        try(DatabaseHelper helper = DatabaseHelper.create()){
            int count = helper.executeStatement("SELECT COUNT(*) FROM users;", (results -> {
                results.next();
                return results.getInt("COUNT(*)");})
            );
            System.out.println("Count: "+count);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
