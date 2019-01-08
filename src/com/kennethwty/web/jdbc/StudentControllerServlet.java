package com.kennethwty.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil dbUtil;

	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	// Note:
	// Override init() from GenericServlet class
	// Tomcat calls this method when the servlet is first loaded or initialized
	// Good for custom code
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create student util
		try {
			dbUtil = new StudentDbUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// list the students with MVC
		try {
			// read the command parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, the default to listing students
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			// route to the appropriate method
			switch(theCommand) {
				case "LIST":
					listStudents(request, response);
					break;
				case "ADD":
					addStudent(request, response);
					break;
				case "LOAD":
					loadStudent(request, response);
					break;
				case "UPDATE":
					updateStudent(request, response);
					break;
				case "DELETE":
					deleteStudent(request, response);
					break;
				default:
					listStudents(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String studentId = request.getParameter("studentId");
		
		// delete student from database
		dbUtil.deleteStudent(studentId);
		
		// send them back to "list student" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		String fn = request.getParameter("firstName");
		String ln = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student theStudent = new Student(fn, ln, email, studentId);
		
		// perform update on database
		dbUtil.updateStudent(theStudent);
		
		// send them back to the "list student" page
		listStudents(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String studentId = request.getParameter("studentId");
		
		// get student fron database (dbUtil)
		Student student = dbUtil.getStudent(studentId);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", student);
		
		//send to jsp page: update-student-form.jsps
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String fn = request.getParameter("firstName");
		String ln = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student newStudent = new Student(fn, ln, email);
		
		// add the student to the database
		dbUtil.addStudent(newStudent);
		
		// send back to main page with the student list
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get students from db util
		List<Student> students = dbUtil.getStudents();
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-student.jsp");
		dispatcher.forward(request, response);
	}

}
