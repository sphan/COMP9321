package edu.unsw.comp9321.jdbc;

public class HotelDTO {
	
	private final int id;
	private final String name;
	private final String location;
	
	public HotelDTO(int id, String name, String location) {
		this.id = id;
		this.name = name;
		this.location = location;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getLocation() {
		return this.location;
	}
}
