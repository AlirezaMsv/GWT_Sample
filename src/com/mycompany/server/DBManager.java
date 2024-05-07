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
            	String parentQuery = "SELECT CONCAT(firstname, \" \", lastname) AS parentName FROM usersinfo WHERE id = ?";
            	String pn = "";
            	try (PreparedStatement statement = conn.prepareStatement(parentQuery)) {
                    statement.setString(1, user.parentID);
                    try (ResultSet resultSet = statement.executeQuery()) {
                    	if (resultSet.next()) {
                    		pn = resultSet.getString("parentName");
                    	}
                    }
                }
                // Prepare SQL statement
                String sql = "INSERT INTO usersInfo (firstname, lastname, age, phoneNum, password, email, parentID, parentName) values (? , ? , ? , ? , ? , ? , ? , ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, user.firstname);
                    statement.setString(2, user.lastname);
                    statement.setString(3, user.age+"");
                    statement.setString(4, user.phoneNum);
                    statement.setString(5, hashPassword(user.password));
                    statement.setString(6, user.email);
                    statement.setString(7, user.parentID);
                    statement.setString(8, pn);


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

    public static ArrayList<HashMap<String, String>> fetchUsers(Integer start, Integer end) {
    	// SQL query to fetch all rows
    	String query = "SELECT id, firstname, lastname, parentID, parentName,  age FROM usersinfo LIMIT " + start + " , " + (end - start);
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
        
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
        	Statement stmt = con.createStatement();
            
        	ResultSet rs = stmt.executeQuery(query);
            // Iterate through the result set
            while (rs.next()) {
            	HashMap<String, String> row = new HashMap<String, String>();
            	row.put("id", rs.getString("id"));
            	row.put("firstname", rs.getString("firstname"));
            	row.put("lastname", rs.getString("lastname"));
            	row.put("parentName", rs.getString("parentName"));
            	row.put("parentID", rs.getString("parentID"));
            	row.put("age", rs.getString("age"));
            	res.add(row);
            }
            ResultSet n = stmt.executeQuery("SELECT FOUND_ROWS() AS n;");
        	if (n.next()){
        		HashMap<String, String> row = new HashMap<String, String>();
        		row.put("n", n.getInt("n")+"");
        		res.add(row);
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return res;
    }

    
    public static HashMap<String, String> fetchUsersDetails(Integer id){
    	try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "SELECT * FROM usersinfo WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, id+"");

                    // Execute query
                    try (ResultSet resultSet = statement.executeQuery()) {
                    	if (resultSet.next()) {
                    		HashMap<String, String> row = new HashMap<String, String>();
                        	row.put("id", resultSet.getString("id"));
                        	row.put("firstname", resultSet.getString("firstname"));
                        	row.put("lastname", resultSet.getString("lastname"));
                        	row.put("age", resultSet.getString("age"));
                        	row.put("email", resultSet.getString("email"));
                        	row.put("phoneNum", resultSet.getString("phoneNum"));
                        	row.put("parentInfo", resultSet.getString("parentID"));
//                        	row.put("parentInfo", resultSet.getString("parentName") == null ? "" :
//                        			resultSet.getString("parentName") + " - " + resultSet.getString("parentID"));
                        	return row;
                    	}	
                    	return null;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        	return null;
        }
    }


    public static Boolean updateUser(User user, Integer id) {
    	try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            	String parentQuery = "SELECT CONCAT(firstname, \" \", lastname) AS parentName FROM usersinfo WHERE id = ?";
            	String pn = "";
            	try (PreparedStatement statement = conn.prepareStatement(parentQuery)) {
                    statement.setString(1, user.parentID);
                    try (ResultSet resultSet = statement.executeQuery()) {
                    	if (resultSet.next()) {
                    		pn = resultSet.getString("parentName");
                    	}
                    }
                }
                // Prepare SQL statement
                String sql = "UPDATE usersInfo SET firstname = ? , lastname = ? , age = ? , phoneNum = ? , email = ? , parentID = ? , parentName = ? WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, user.firstname);
                    statement.setString(2, user.lastname);
                    statement.setString(3, user.age+"");
                    statement.setString(4, user.phoneNum);
                    statement.setString(5, user.email);
                    statement.setString(6, user.parentID);
                    statement.setString(7, pn);
                    statement.setString(8, id+"");

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

    
    public static Boolean removeSelected(String[] ids) {
    	try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "DELETE FROM usersInfo WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                	for (String i : ids) {
                		statement.setInt(1, Integer.parseInt(i));
                		
                		// Execute query
                		System.out.println(statement.executeUpdate());
                	}
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
   
    
    public static ArrayList<HashMap<String, String>> fetchCombo(Integer id, Integer start, Integer end, String search){
    	// SQL query to fetch all rows
    	String query = "SELECT SQL_CALC_FOUND_ROWS id, firstname, lastname, parentName, parentID FROM usersinfo WHERE id <> " + id + " AND (firstname LIKE '%" + search +
    			"%' OR lastname LIKE '%" + search + "%' OR id LIKE '%" + search + "%') LIMIT " + start + " , " + (end - start)
    			;
    	
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String,String>>();
        
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
        	Statement stmt = con.createStatement();
            
            ResultSet rs = stmt.executeQuery(query);
         
            // Iterate through the result set
            while (rs.next()) {
            	HashMap<String, String> row = new HashMap<String, String>();
            	row.put("id", rs.getString("id"));
            	row.put("name", rs.getString("firstname") + " " + rs.getString("lastname") + " ( " + rs.getString("id") + " )");
            	res.add(row);
            }
         // Get the number of rows without running the query again
            try (ResultSet n = stmt.executeQuery("SELECT FOUND_ROWS() AS n;")) {
                if (n.next()) {
                    int rowCount = n.getInt("n");
                    HashMap<String, String> row = new HashMap<String, String>();
                	row.put("n", rowCount+"");
                	res.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return res;
    }
}
