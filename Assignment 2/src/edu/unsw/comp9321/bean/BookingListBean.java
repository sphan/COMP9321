package edu.unsw.comp9321.bean;

import java.util.ArrayList;
import java.util.List;

public class BookingListBean {
	
	private List<BookingSelection> list;
	
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

}
