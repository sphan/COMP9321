package edu.unsw.comp9321.bean;

public class BookingSelection {
	
	private int index;
	private String roomType;
	private int price;
	
	public BookingSelection(int index, String roomType, int price) {
		this.index = index;
		this.roomType = roomType;
		this.price = price;
	}
	public BookingSelection(int index, String roomType, String price) {
		this(index, roomType, Integer.parseInt(price));
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

}
