package edu.unsw.comp9321;

import java.util.HashMap;
import java.util.List;

public class Hotel {
	public Hotel(String name) {
		this.name = name;
		this.rooms = new HashMap<RoomType, List<Room>>();
	}
	
	/**
	 * Get the name of the hotel.
	 * @return The name of the hotel.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the hotel.
	 * @param name The name to be set for the hotel.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get all the rooms in the hotel.
	 * @return All the rooms in the hotel.
	 */
	public HashMap<RoomType, List<Room>> getAllRooms() {
		return rooms;
	}
	
	/**
	 * Given a type of room, get all the rooms of that type.
	 * @param roomType The type of room searching for.
	 * @return The list of rooms of the type being searched for.
	 */
	public List<Room> getRoomByType(RoomType roomType) {
		return rooms.get(roomType);
	}
	
	/**
	 * Add room to the list of room according to its type.
	 * @param room The room to be added.
	 * @param roomType The type of the room.
	 */
	public void addRoom(Room room, RoomType roomType) {
		rooms.get(roomType).add(room);
	}

	private String name;
	private HashMap<RoomType, List<Room>> rooms;
}
