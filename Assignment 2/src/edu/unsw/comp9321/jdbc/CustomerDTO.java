package edu.unsw.comp9321.jdbc;

public class CustomerDTO {
	
	private final int id;
	private String name;
	private String userName;
	
	public CustomerDTO (int id, String name, String userName) {
		this.id = id;
		this.setName(name);
		this.setUserName(userName);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
