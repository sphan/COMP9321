package edu.unsw.comp9321.bean;

public class OwnerPriceBean {
	private int currentPrice;
	private String roomType;
	private int discountPrice;
	private String discountStartDate;
	private String discountEndDate;
	private String location;
	
	public OwnerPriceBean(int current_price, String room_type,
			int discount_price, String start_date, String end_date, String location) {
		this.currentPrice = current_price;
		this.roomType = room_type;
		this.discountPrice = discount_price;
		this.discountStartDate = start_date;
		this.discountEndDate = end_date;
		this.location = location;
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public String getRoomType() {
		return roomType;
	}

	public int getDiscountPrice() {
		return discountPrice;
	}

	public String getDiscountStartDate() {
		return discountStartDate;
	}

	public String getDiscountEndDate() {
		return discountEndDate;
	}

	public String getLocation() {
		return location;
	}
}
