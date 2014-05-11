package edu.unsw.comp9321.bean;

import edu.unsw.comp9321.jdbc.BookingDTO;

public class URLBookingBean {
	
	private String code;
	private BookingDTO booking;
	
	public URLBookingBean() {
		this.setCode(null);
		this.setBooking(null);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BookingDTO getBooking() {
		return booking;
	}

	public void setBooking(BookingDTO booking) {
		this.booking = booking;
	}
	

}
