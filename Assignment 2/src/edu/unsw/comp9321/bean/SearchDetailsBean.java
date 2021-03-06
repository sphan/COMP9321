package edu.unsw.comp9321.bean;

import edu.unsw.comp9321.logic.Command;

public class SearchDetailsBean {
	
	private int startDay;
	private int startMonth;
	private int startYear;
	
	private int endDay;
	private int endMonth;
	private int endYear;
	
	private String location;
	private int maxPrice;
	
	public SearchDetailsBean() {
		this.setStartDay(Command.getCurrentDay());
		this.setStartMonth(Command.getCurrentMonth());
		this.setStartYear(Command.getCurrentYear());
		this.setEndDay(Command.getCurrentDayPlus());
		this.setEndMonth(Command.getCurrentMonthPlus());
		this.setEndYear(Command.getCurrentYearPlus());
		this.setLocation("Select");
		this.setMaxPrice(500);
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
	public int getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
}
