package edu.unsw.comp9321.jdbc;

public class RoomTypeDTO {
	
	private String roomType;
	private int price;
	private int count;
	private int selectValue;//selectValue used only for passing on count values on jsp page
	private boolean discounted;
	private boolean peaked;
	
	public RoomTypeDTO(String roomType, int price, int count, int selectValue) {
		this.roomType = roomType;
		this.price = price;
		this.count = count;
		discounted = false;
		peaked = false;
	}
	public boolean isDiscounted() {
		return discounted;
	}
	
	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}
	
	public boolean isPeaked() {
		return peaked;
	}
	
	public void setPeaked(boolean peaked) {
		this.peaked = peaked;
	}
	
	public RoomTypeDTO(String roomType, int price, int count) {
		this(roomType, price, count, 0);
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
	public int getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(int selectValue) {
		this.selectValue = selectValue;
	}
	

}
