package com.kennethwty.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define datasource/connection pool for Resource Injection (has to match with the name defined in context.xml
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set up the printWriter and content type
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		// Get a connection to the database
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			conn = dataSource.getConnection();
			
			// Create a SQL statements
			String sql = "SELECT * FROM student";
			statement = conn.createStatement();
			
			// Execute SQL query
			resultSet = statement.executeQuery(sql);
			
			// Process the result set
			while(resultSet.next()) {
				String email = resultSet.getString("email");
				out.println(email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
