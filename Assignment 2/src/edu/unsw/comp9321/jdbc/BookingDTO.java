package edu.unsw.comp9321.jdbc;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BookingDTO {

	private int id;
	private CustomerDTO customer;
	private Calendar startDate;
	private Calendar endDate;
	private List<RoomScheduleDTO> roomSchedules;
	private HotelDTO hotel;

	public BookingDTO(int id, CustomerDTO customer, Calendar startDate, Calendar endDate, List<RoomScheduleDTO> rooms, HotelDTO hotel) {
		this.id = id;
		this.customer = customer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.roomSchedules = rooms;
		this.hotel = hotel;
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

	public String getStartDateString() {
		return new SimpleDateFormat("dd-MM-yyyy").format(startDate.getTime());
	}
	public String getEndDateString() {
		return new SimpleDateFormat("dd-MM-yyyy").format(endDate.getTime());
	}
	
	public int getStartDay() {
		return Integer.parseInt(new SimpleDateFormat("dd").format(startDate.getTime()));
	}
	public int getStartMonth() {
		return Integer.parseInt(new SimpleDateFormat("MM").format(startDate.getTime()));
	}
	public int getStartYear() {
		return Integer.parseInt(new SimpleDateFormat("yyyy").format(startDate.getTime()));
	}
	public int getEndDay() {
		return Integer.parseInt(new SimpleDateFormat("dd").format(endDate.getTime()));
	}
	public int getEndMonth() {
		return Integer.parseInt(new SimpleDateFormat("MM").format(endDate.getTime()));
	}
	public int getEndYear() {
		return Integer.parseInt(new SimpleDateFormat("yyyy").format(endDate.getTime()));
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

	public List<RoomScheduleDTO> getRoomSchedules() {
		return roomSchedules;
	}

	public void setRooms(List<RoomScheduleDTO> roomSchedules) {
		this.roomSchedules = roomSchedules;
	}

	public HotelDTO getHotel() {
		return hotel;
	}

	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}
}
