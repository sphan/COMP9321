package edu.unsw.comp9321;

public class Room {
	public Room(String name) {
		this.name = name;
		this.price = 0;
		this.type = RoomType.SINGLE;
	}
	
	/**
	 * Get the name of the room.
	 * @return Name of the room.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the room.
	 * @param name The name to be set for the room.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the price for the room.
	 * Will be more complicated than this. Have to consider
	 * peak and off-peak. May be set this as the standard price
	 * and have a special function to calculate all prices according
	 * to the booking day.
	 * @return The price for the room.
	 */
	public float getPrice() {
		return price;
	}
	
	/**
	 * Set the price for the room.
	 * @param price Price for the room.
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
	/**
	 * Get the room type. Type ranges in Single, Twin,
	 * Queen, Executive, Suite.
	 * @return The room type.
	 */
	public RoomType getType() {
		return type;
	}
	
	/**
	 * Set the type of the room in one of the following:
	 * Single, Twin, Queen, Executive, Suite.
	 * @param type The type of the room.
	 */
	public void setType(RoomType type) {
		this.type = type;
	}

	private String name;
	private float price;
	private RoomType type;
}
