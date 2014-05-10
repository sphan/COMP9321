package edu.unsw.comp9321.bean;

public class BookingSelection {
	
	private int index;
	private String roomType;
	private int price;
	private boolean extraBed;
	
	public BookingSelection(int index, String roomType, int price, boolean extraBed) {
		this.index = index;
		this.roomType = roomType;
		this.price = price;
		this.extraBed = extraBed;
	}
	public BookingSelection(int index, String roomType, String price, boolean extraBed) {
		this(index, roomType, Integer.parseInt(price), extraBed);
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
	public boolean isExtraBed() {
		return extraBed;
	}
	public void setExtraBed(boolean extraBed) {
		this.extraBed = extraBed;
	}

}
