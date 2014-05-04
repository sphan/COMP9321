package edu.unsw.comp9321.jdbc;

import java.sql.Date;

public class BookingDTO {
	
	private Date startDate;
	private Date endDate;
	private CustomerDTO customer;
	
	public BookingDTO (Date startDate, Date endDate, CustomerDTO customer) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setCustomer(customer);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

}
