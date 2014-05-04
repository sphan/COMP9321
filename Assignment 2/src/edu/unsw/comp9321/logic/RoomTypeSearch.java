package edu.unsw.comp9321.logic;

public class RoomTypeSearch {
	
	private String roomType;
	private int price;
	private int count;
	
	public RoomTypeSearch(String roomType, int price, int count) {
		this.roomType = roomType;
		this.price = price;
		this.count = count;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

}
