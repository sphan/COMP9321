package edu.unsw.comp9321.jdbc;

public class CustomerDTO {
	
	private final int id;
	private String firstName;
	private String lastName;
	
	public CustomerDTO (int id, String firstName, String lastName) {
		this.id = id;
		this.setName(firstName);
		this.setUserName(lastName);
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setUserName(String lastName) {
		this.lastName = lastName;
	}
}
