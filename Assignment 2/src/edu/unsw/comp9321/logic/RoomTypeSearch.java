package edu.unsw.comp9321.logic;

public class RoomTypeSearch {
	
	private String roomType;
	private int price;
	
	public RoomTypeSearch(String roomType, int price) {
		this.roomType = roomType;
		this.price = price;
	}
	
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	

}
