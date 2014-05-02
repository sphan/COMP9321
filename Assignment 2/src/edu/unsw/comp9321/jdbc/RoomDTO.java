package edu.unsw.comp9321.jdbc;

public class RoomDTO {
	
	private int id;
	private int room_number;
	private float price;
	private float discounted_price;
	private int room_type;
	private int availability;
	private int hotel;
	
	public RoomDTO (int id, int room_number, float price, float discounted_price, int room_type, int availability, int hotel) {
		setId(id);
		setRoom_number(room_number);
		setPrice(price);
		setDiscounted_price(discounted_price);
		setRoom_type(room_type);
		setAvailability(availability);
		setHotel(hotel);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoom_number() {
		return room_number;
	}

	public void setRoom_number(int room_number) {
		this.room_number = room_number;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscounted_price() {
		return discounted_price;
	}

	public void setDiscounted_price(float discounted_price) {
		this.discounted_price = discounted_price;
	}

	public int getRoom_type() {
		return room_type;
	}

	public void setRoom_type(int room_type) {
		this.room_type = room_type;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}
	
	
	/*id int not null generated always as identity,
	room_number smallint,
	price float(10),
	discounted_price float(20),
	room_type smallint,
	availability smallint,
	hotel int,*/

}
