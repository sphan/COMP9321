package edu.unsw.comp9321.jdbc;

import java.io.Serializable;
import java.util.Calendar;

public class PeakPeriodDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar startDate;
	private Calendar endDate;
	
	public PeakPeriodDTO(int startday, int startmonth, int endday, int endmonth) {
		startDate = Calendar.getInstance();
		startDate.set(Calendar.DATE, startday);
		startDate.set(Calendar.MONTH, startmonth);
		
		endDate = Calendar.getInstance();
		endDate.set(Calendar.DATE, endday);
		endDate.set(Calendar.MONTH, endmonth);
		
		if (endmonth < startmonth)
			endDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) + 1);
	}

	public Calendar getStartDate() {
		return startDate;
	}
	
	public String getStartDateString() {
		return startDate.get(Calendar.YEAR) + "-" + (startDate.get(Calendar.MONTH) + 1) + "-" + startDate.get(Calendar.DATE); 
	}

	public Calendar getEndDate() {
		return endDate;
	}
	
	public String getEndDateString() {
		return endDate.get(Calendar.YEAR) + "-" + (endDate.get(Calendar.MONTH) + 1) + "-" + endDate.get(Calendar.DATE); 
	}
	
	public boolean isInPeak(Calendar start, Calendar end) {
		if (start.after(startDate) && start.before(endDate)) {
			if (end.before(endDate))
				return true;
		}
		return false;
	}
}
