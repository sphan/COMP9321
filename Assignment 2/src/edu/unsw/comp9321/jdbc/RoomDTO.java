package edu.unsw.comp9321.jdbc;

public class RoomDTO {
	
	private int id;
	private int room_number;
	private float price;
	private float discounted_price;
	private RoomType room_type;
	private Availability availability;
	private int hotel;
	
	public RoomDTO (int id, int room_number, float price, float discounted_price, String room_type, String availability, int hotel) {
		setId(id);
		setRoom_number(room_number);
		setPrice(price);
		setDiscounted_price(discounted_price);
		setRoom_type(room_type);
		setAvailability(availability);
		setHotel(hotel);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoom_number() {
		return this.room_number;
	}

	public void setRoom_number(int room_number) {
		this.room_number = room_number;
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

	public void setRoom_type(String room_type) {
		//replaceAll removes any extra white space padding that came from somewhere
		this.room_type = RoomType.valueOf(room_type.toUpperCase().replaceAll("\\W", ""));
	}

	public Availability getAvailability() {
		return this.availability;
	}

	public void setAvailability(String availability) {
		//replaceAll removes any extra white space padding that came from somewhere
		this.availability = Availability.valueOf(availability.toUpperCase().replaceAll("\\W", ""));
	}

	public int getHotel() {
		return this.hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

}
