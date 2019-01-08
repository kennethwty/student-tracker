package com.kennethwty.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			// get a connection
			conn = dataSource.getConnection();
			
			// create sql statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			statement = conn.createStatement();
			
			// execute query
			result = statement.executeQuery(sql);
			
			// process result set
			while(result.next()) {
				// retrieve data from result set row
				int id = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String email = result.getString("email");
				
				// create new student objects
				Student student = new Student(firstName, lastName, email, id);
				
				// add it to the list of students
				students.add(student);
			}
			
			return students;
			
		} finally {
			// close all JDBC objects
			close(conn, statement, result);
		}
	}
	
	private void close(Connection conn, Statement statement, ResultSet result) {
		try {
			if(result != null) result.close();
			if(statement != null) statement.close();
			// gets put back into the connection pool, not actually closing the connection to the db
			if(conn != null) conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student newStudent) throws Exception {
		Connection conn = null;
		PreparedStatement pStatement = null;
		
		try {
			// set up DB connection
			conn = dataSource.getConnection();
			
			// create sql for insert
			String sql = "INSERT INTO student (first_name, last_name, email) VALUES (?, ?, ?)";
			pStatement = conn.prepareStatement(sql);
			
			// set the param values for the student
			pStatement.setString(1, newStudent.getFirstName());
			pStatement.setString(2, newStudent.getLastName());
			pStatement.setString(3, newStudent.getEmail());
			
			// execute sql inserts
			pStatement.executeUpdate();
			
		} finally {
			// clean up JDBC objects
			close(conn, pStatement, null);
		}
	}

	public Student getStudent(String studentId) throws SQLException {
		Student theStudent = null;
		
		Connection conn = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		int id;
		
		try {
			// convert student id to int
			id = Integer.parseInt(studentId);
			
			// get connection to database
			conn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "SELECT * FROM student WHERE id = ?";
			
			// create prepared statement
			pStatement = conn.prepareStatement(sql);
			
			// set params
			pStatement.setInt(1, id);
			
			// execute statement
			resultSet = pStatement.executeQuery();
			
			// retrieve data from result set row
			if(resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String email = resultSet.getString("email");
				
				// create the Student object
				theStudent = new Student(firstName, lastName, email, id);
			} else {
				throw new SQLException("Coudl not locate student in database with id : " + id);
			}
			
			return theStudent;
		} finally {
			close(conn, pStatement, resultSet);
		}
	}

	public void updateStudent(Student theStudent) throws SQLException {
		Connection conn = null;
		PreparedStatement pStatement = null;
		
		try {
			// get db connection
			conn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";
			
			// prepare statement
			pStatement = conn.prepareStatement(sql);
			
			// set params
			pStatement.setString(1, theStudent.getFirstName());
			pStatement.setString(2, theStudent.getLastName());
			pStatement.setString(3, theStudent.getEmail());
			pStatement.setInt(4, theStudent.getId());
			
			// execute SQL statement
			pStatement.executeUpdate();
			
		} finally {
			close(conn, pStatement, null);
		}
	}

	public void deleteStudent(String studentId) throws SQLException {
		Connection conn = null;
		PreparedStatement pStatement = null;
		
		try {
			// get db connection
			conn = dataSource.getConnection();

			// create SQL update statement
			String sql = "DELETE FROM student WHERE id=?";

			// prepare statement
			pStatement = conn.prepareStatement(sql);

			// set params
			pStatement.setInt(1, Integer.parseInt(studentId));

			// execute SQL statement
			pStatement.executeUpdate();
		} finally {
			close(conn, pStatement, null);
		}
	}
}
