package com.mycompany.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;
import com.mycompany.shared.User;

public class DBManager {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/rastak";
    private static final String DB_USER = "rastak";
    private static final String DB_PASSWORD = "rastak";
    
    private static String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();

        // Hash the password with the generated salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }
    
    private static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static boolean validateUser(String username, String password) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "SELECT password FROM usersinfo WHERE email = ?";
//                String sql = "SELECT * FROM usersinfo WHERE email = ? AND password = ?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
//                	password = hashPassword(password);
//                	Window.alert(password);
                    statement.setString(1, username);
//                    statement.setString(2, password);

                    // Execute query
                    try (ResultSet resultSet = statement.executeQuery()) {
                    	if (resultSet.next()) {
                    		return verifyPassword(password, resultSet.getString("password"));
                    	}	
                    	return false;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public static boolean addUser(User user) {
    	try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "INSERT INTO usersInfo (firstname, lastname, age, phoneNum, password, email) values (? , ? , ? , ? , ? , ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, user.firstname);
                    statement.setString(2, user.lastname);
                    statement.setString(3, user.age+"");
                    statement.setString(4, user.phoneNum);
                    statement.setString(5, hashPassword(user.password));
                    statement.setString(6, user.email);

                    // Execute query
                    System.out.println(statement.executeUpdate());
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<HashMap<String, String>> fetchUsers() {
    	// SQL query to fetch all rows
        String query = "SELECT * FROM usersinfo";
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
        
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set
            while (rs.next()) {
//                jsonObject.addProperty("firstname", rs.getString("firstname"));
            	HashMap<String, String> row = new HashMap<String, String>();
            	row.put("firstname", rs.getString("firstname"));
            	row.put("lastname", rs.getString("lastname"));
            	row.put("age", rs.getString("age"));
            	row.put("email", rs.getString("email"));
            	row.put("phoneNum", rs.getString("phoneNum"));
            	res.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return res;
    }
}
