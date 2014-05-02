package edu.unsw.comp9321.jdbc;

public class CustomerDTO {
	
	private final int id;
	private String name;
	private String userName;
	private String password;
	
	public CustomerDTO (int id, String name, String userName, String password) {
		this.id = id;
		this.setName(name);
		this.setUserName(userName);
		this.setPassword(password);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
