package edu.unsw.comp9321.jdbc;

public class CustomerDTO {
	
	private final int id;
	private String first_name;
	private String last_name;
	
	public CustomerDTO (int id, String firstName, String lastName) {
		this.id = id;
		this.setName(firstName);
		this.setUserName(lastName);
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setName(String firstName) {
		this.first_name = firstName;
	}

	public String getLastName() {
		return last_name;
	}

	public void setUserName(String lastName) {
		this.last_name = lastName;
	}
}
