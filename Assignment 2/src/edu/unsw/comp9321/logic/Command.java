package edu.unsw.comp9321.logic;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.unsw.comp9321.jdbc.Availability;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.StaffDTO;
import edu.unsw.comp9321.jdbc.StaffType;

public class Command {
	public static String search(HttpServletRequest request, DAO dao) {
		String nextPage = "";
		
		return nextPage;
	}
	
	public static String login(HttpServletRequest request, DAO dao) {
		String nextPage = "customerMain.jsp";
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		StaffDTO staff = dao.getStaffByUsername(username);
		if (staff != null) {
			if (verifyLogin(staff, password)) {
				if (staff.getType() == StaffType.MANAGER) {
					nextPage = "staffPage.jsp";
					displayAllBookings(request, dao);
				}
				request.setAttribute("staffName", staff.getName());
			}
		}
		
		return nextPage;
	}
	
	public static void displayAllBookings(HttpServletRequest request, DAO dao) {
		List<BookingDTO> allBookings = dao.getAllBookings();
		System.out.println(allBookings.size());
//		List<BookingDTO> pendingBookings = new LinkedList<BookingDTO>();
		
//		for (BookingDTO booking : allBookings) {
//			if (booking.getEndDate().after(Calendar.getInstance())) {
//				pendingBookings.add(booking);
//				System.out.println("booking: " + booking.getId());
//			}
//		}
		
//		List<BookingDTO> booked = new LinkedList<BookingDTO>();
//		List<BookingDTO> checkedin = new LinkedList<BookingDTO>();
//		for (BookingDTO booking: allBookings) {
//			if (booking.getRooms().get(0).getAvailability() == Availability.BOOKED) {
//				booked.add(booking);
//				System.out.println("booked: " + booking.getId());
//			} else if (booking.getRooms().get(0).getAvailability() == Availability.CHECKEDIN) {
//				checkedin.add(booking);
//				System.out.println("checkedin: " + booking.getId());
//			}
//		}
		
		request.setAttribute("booked", allBookings);
//		request.setAttribute("checkedin", checkedin);
	}
	
	/***********************************************
	 * HELPER FUNCTIONS
	 **********************************************/
	public static boolean verifyLogin(StaffDTO staff, String password) {
		if (staff.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
}
