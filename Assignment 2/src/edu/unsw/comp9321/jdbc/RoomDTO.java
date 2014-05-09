package edu.unsw.comp9321.jdbc;

public class RoomDTO {

	private final int id;
	private final int roomNumber;
	private final RoomType roomType;
	private Availability availability;
	private final int hotelID;

	public RoomDTO (int id, int roomNumber, String roomType, String availability, int hotelID) {
		this.id = id;
		this.roomNumber = roomNumber;
		//replaceAll removes any extra white space padding that came from somewhere
		this.roomType = RoomType.valueOf(roomType.toUpperCase().replaceAll("\\W", ""));
		this.availability = Availability.valueOf(availability.toUpperCase().replaceAll("\\W", ""));
		this.hotelID = hotelID;
	}

	public int getId() {
		return this.id;
	}

	public int getRoomNumber() {
		return this.roomNumber;
	}

	public RoomType getRoomType() {
		return this.roomType;
	}

	public Availability getAvailability() {
		return this.availability;
	}
	
	public void setAvailability(String availability) {
		//if availability is set from a string form
		setAvailability(Availability.valueOf(availability.toUpperCase()));
	}
	
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public int getHotel() {
		return this.hotelID;
	}
}
