package edu.unsw.comp9321.logic;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.unsw.comp9321.jdbc.Availability;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.CustomerDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.HotelDTO;
import edu.unsw.comp9321.jdbc.RoomDTO;
import edu.unsw.comp9321.jdbc.StaffDTO;
import edu.unsw.comp9321.jdbc.StaffType;

public class Command {
	public static String ownerSearch(HttpServletRequest request, DAO dao) {
		String nextPage = "ownerPage.jsp";
		
		String hotelLocation = request.getParameter("hotelLocation");
		System.out.println(hotelLocation);
		String roomAvail = request.getParameter("roomAvail");
		System.out.println(roomAvail);
		
		HashMap<String, HashMap<String, Integer>> occupancies;
		
		if (roomAvail.equalsIgnoreCase("all")) {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation);
			printOccupancies(occupancies);
		} else {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation, roomAvail.toUpperCase());
		}
		
		HashMap<String, HashMap<String, HashMap<String, Integer>>> results =
				new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
		results.put(hotelLocation, occupancies);
		
		request.setAttribute("occupancies", results);
		
		return nextPage;
	}
	
	public static String staffLogin(HttpServletRequest request, DAO dao) {
		String nextPage = "customerMain.jsp";
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		StaffDTO staff = dao.getStaffByUsername(username);
		if (staff != null) {
			if (verifyLogin(staff, password)) {
				if (staff.getType() == StaffType.MANAGER) {
					nextPage = "staffPage.jsp";
					displayAllBookings(request, dao);
				} else if (staff.getType() == StaffType.OWNER) {
					nextPage = "ownerPage.jsp";
					displayAllOccupancies(request, dao);
				}
				request.getSession().setAttribute("staffName", staff.getName());
//				request.setAttribute("staffName", staff.getName());
			}
		}
		
		return nextPage;
	}
	
	public static void displayAllBookings(HttpServletRequest request, DAO dao) {
		List<BookingDTO> allBookings = dao.getAllBookings();
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
		request.setAttribute("resultNum", allBookings.size());
//		request.setAttribute("checkedin", checkedin);
	}
	
	public static String staffSelectBooking(HttpServletRequest request, DAO dao) {
		String nextPage = "checkInPage.jsp";
		
		int bookingID = 0;
		if (request.getParameter("bookingID") == null) {
			return "staffPage.jsp";
		} else {
			try {
				bookingID = Integer.parseInt(request.getParameter("bookingID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		List<RoomDTO> rooms = dao.getRoomsByBooking(bookingID);
		CustomerDTO customer = dao.getCustomerByBookingID(bookingID);
				
		request.setAttribute("customer", customer);
		request.setAttribute("rooms", rooms);
		return nextPage;
	}
	
	public static String staffSearch(HttpServletRequest request, DAO dao) {
		String nextPage = "staffPage.jsp";
		String searchType = request.getParameter("search-type");
		String searchString = request.getParameter("searchString");
		List<BookingDTO> bookings = new LinkedList<BookingDTO>();
		
		if (searchType.equalsIgnoreCase("bookingNumber")) {
			int bookingID;
			try {
				bookingID = Integer.parseInt(searchString);
				bookings.add(dao.getBookingByID(bookingID));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (searchType.equalsIgnoreCase("customerName")) {
			bookings = dao.getBookingsByCustomerName(searchString);
		}
		
		request.setAttribute("booked", bookings);
		request.setAttribute("resultNum", bookings.size());
		request.setAttribute("staffName", request.getParameter("staffName"));
		
		return nextPage;
	}
	
	public static void displayAllOccupancies(HttpServletRequest request, DAO dao) {
		List<HotelDTO> hotels = dao.getAllHotelLocations();
		HashMap<String, HashMap<String, HashMap<String, Integer>>> results =
				new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
		
		for (HotelDTO hotel : hotels) {
			HashMap<String, HashMap<String, Integer>> occupancies = dao.getRoomsOccupancyByLocation(hotel.getLocation());
//			printOccupancies(occupancies);
			if (occupancies.size() > 0)
				results.put(hotel.getLocation(), occupancies);
		}
		
		request.setAttribute("occupancies", results);
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
	
	public static void printOccupancies(HashMap<String, HashMap<String, Integer>> occupancies) {
		for (String roomtype : occupancies.keySet()) {
			for (String avail : occupancies.get(roomtype).keySet()) {
				System.out.println(roomtype + ": " + avail + " " + occupancies.get(roomtype).get(avail));
			}
		}
	}
}
