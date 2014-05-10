package edu.unsw.comp9321.jdbc;

public class RoomScheduleDTO {
	
	private int id;
	private RoomDTO room;
	private String roomType;
	private final boolean extraBed;
	private int customerID;
	
	public RoomScheduleDTO (int id, RoomDTO room, String roomType, boolean extraBed, int customerID) {
		this.setId(id);
		this.setRoom(room);
		this.setRoomType(roomType);
		this.extraBed = extraBed;
		this.setCustomerID(customerID);
	}

	public RoomDTO getRoom() {
		return room;
	}

	public void setRoom(RoomDTO room) {
		this.room = room;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public boolean getExtraBed() {
		return extraBed;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
