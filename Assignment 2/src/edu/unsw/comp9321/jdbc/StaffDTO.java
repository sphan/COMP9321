package edu.unsw.comp9321.jdbc;

public class StaffDTO {
	private final int id;
	private String name;
	private String userName;
	private String password;
	private StaffType type;
	
	public StaffDTO (int id, String name, String userName, String password) {
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
	
	public StaffType getType() {
		return type;
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

	public void setType(String staff_type) {
		this.type = StaffType.valueOf(staff_type.toUpperCase().replaceAll("\\W", ""));
	}
}
