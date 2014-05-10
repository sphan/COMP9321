package edu.unsw.comp9321.bean;

import edu.unsw.comp9321.jdbc.HotelDTO;
import edu.unsw.comp9321.jdbc.RoomDTO;

public class BookingRoomDetailBean {
	private String roomType;
	private HotelDTO hotel;
	private RoomDTO room;
	
	public BookingRoomDetailBean(String roomType, HotelDTO hotel, RoomDTO room) {
		this.roomType = roomType;
		this.hotel = hotel;
		this.room = room;
	}

	public String getRoomType() {
		return roomType;
	}

	public HotelDTO getHotel() {
		return hotel;
	}

	public RoomDTO getRoom() {
		return room;
	}
}
