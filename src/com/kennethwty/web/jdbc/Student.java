package com.kennethwty.web.jdbc;

public class Student {
	private String firstName;
	private String lastName;
	private String email;
	private int id;
	
	/**
	 * Constructor without ID
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public Student(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Student(String firstName, String lastName, String email, int id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Student [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", id=" + id + "]";
	}
	
	
}
