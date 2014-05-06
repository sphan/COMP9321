package edu.unsw.comp9321.bean;

public class BookingSelection {
	
	private String roomType;
	private int count;
	private int price;
	
	public BookingSelection(String roomType, int count, int price) {
		this.roomType = roomType;
		this.count = count;
		this.price = price;
	}
	public BookingSelection(String roomType, String count, String price) {
		this(roomType, Integer.parseInt(count), Integer.parseInt(price));
	}
	
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

}
