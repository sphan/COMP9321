package edu.unsw.comp9321.bean;


public class OccupancyBean {
	private String roomType;
	private String availability;
	private int roomCount;
	
	public OccupancyBean(String room_type, String availability, int count) {
		this.roomType = room_type;
		this.availability = availability;
		this.roomCount = count;
	}

	public String getRoomType() {
		return roomType;
	}

	public String getAvailability() {
		return availability;
	}

	public int getRoomCount() {
		return roomCount;
	}
}
