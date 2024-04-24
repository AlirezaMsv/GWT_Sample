package com.mycompany.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/rastak";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "rastak";

    public static boolean validateUser(String username, String password) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare SQL statement
                String sql = "SELECT * FROM usersinfo WHERE email = ? AND password = ?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, username);
                    statement.setString(2, password);

                    // Execute query
                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next(); // User found if result set is not empty
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    

}
