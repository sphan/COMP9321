package edu.unsw.comp9321.jdbc;

public class RoomDTO {

	private final int id;
	private final int room_number;
	private float price;
	private float discounted_price;
	private final RoomType room_type;
	private Availability availability;
	private final int hotel;

	public RoomDTO (int id, int room_number, float price, float discounted_price, String room_type, String availability, int hotel) {
		this.id = id;
		this.room_number = room_number;
		this.price = price;
		this.discounted_price = discounted_price;
		//replaceAll removes any extra white space padding that came from somewhere
		this.room_type = RoomType.valueOf(room_type.toUpperCase().replaceAll("\\W", ""));
		this.availability = Availability.valueOf(availability.toUpperCase().replaceAll("\\W", ""));
		this.hotel = hotel;
	}

	public int getId() {
		return this.id;
	}

	public int getRoom_number() {
		return this.room_number;
	}

	public float getPrice() {
		return this.price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscounted_price() {
		return this.discounted_price;
	}
	
	public void setDiscounted_price(float discounted_price) {
		this.discounted_price = discounted_price;
	}

	public RoomType getRoom_type() {
		return this.room_type;
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
		return this.hotel;
	}
}
