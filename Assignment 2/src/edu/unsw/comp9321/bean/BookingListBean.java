package edu.unsw.comp9321.bean;

import java.util.ArrayList;
import java.util.List;

public class BookingListBean {
	
	private List<BookingSelection> list;
	private int startDay;
	private int startMonth;
	private int startYear;
	
	private int endDay;
	private int endMonth;
	private int endYear;
	
	private String location;
	
	public BookingListBean() {
		this.list = new ArrayList<BookingSelection>();
	}

	public List<BookingSelection> getList() {
		return list;
	}

	public void setList(List<BookingSelection> list) {
		this.list = list;
	}
	
	public void addBookingSelection(BookingSelection bs) {
		this.list.add(bs);
	}
	
	public void clearBookingList() {
		this.list = new ArrayList<BookingSelection>();
	}
	
	public int getSize() {
		return this.list.size();
	}
	
	public int getTotalPrice() {
		int total = 0;
		for (BookingSelection bs : list) {
			if (bs.isExtraBed()) {
				total += 35;
			}
			total += bs.getPrice();
		}
		return total;
	}
	
	public int getStartDay() {
		return startDay;
	}
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	public int getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getEndDay() {
		return endDay;
	}
	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}
	public int getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
