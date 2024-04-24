package com.mycompany.server;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "login", urlPatterns = "/login")
public class Login extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve username and password from request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate username and password (you should also perform hashing and validation here)
        boolean isValidUser = DBManager.validateUser(username, password);
        
        // Send response back to the client
        if (isValidUser) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Login successful");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Invalid username or password");
        }
    }
}
