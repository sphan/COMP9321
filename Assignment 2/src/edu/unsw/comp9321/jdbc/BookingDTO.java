package edu.unsw.comp9321.jdbc;


import java.util.Calendar;
import java.util.List;

public class BookingDTO {
	
	private int id;
	private CustomerDTO customer;
	private Calendar startDate;
	private Calendar endDate;
	private List<RoomDTO> rooms;
	
	public BookingDTO(int id, CustomerDTO customer, Calendar startDate, Calendar endDate, List<RoomDTO> rooms) {
		this.id = id;
		this.customer = customer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rooms = rooms;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public CustomerDTO getCustomer() {
		return customer;
	}
	
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public Calendar getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	public List<RoomDTO> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<RoomDTO> rooms) {
		this.rooms = rooms;
	}
}
