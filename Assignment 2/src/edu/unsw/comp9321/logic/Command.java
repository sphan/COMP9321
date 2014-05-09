package edu.unsw.comp9321.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import edu.unsw.comp9321.bean.OccupancyBean;
import edu.unsw.comp9321.jdbc.Availability;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.CustomerDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.HotelDTO;
import edu.unsw.comp9321.jdbc.RoomDTO;
import edu.unsw.comp9321.jdbc.RoomType;
import edu.unsw.comp9321.jdbc.StaffDTO;
import edu.unsw.comp9321.jdbc.StaffType;

public class Command {
	public static String ownerSearch(HttpServletRequest request, DAO dao) {
		String nextPage = "ownerPage.jsp";
		
		String hotelLocation = request.getParameter("hotelLocation");
		String roomAvail = request.getParameter("roomAvail").toLowerCase().replaceAll("\\W", "");
		
		List<OccupancyBean> occupancies = null;
		
		System.out.println("hotelLocation: " + hotelLocation);
		
		if (hotelLocation.isEmpty()) {
			displayAllOccupancies(request, dao);
			return nextPage;
		}
		
		if (roomAvail.equalsIgnoreCase("all")) {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation);
		} else {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation, roomAvail);
		}
		
		HashMap<String, List<OccupancyBean>> results = new HashMap<String, List<OccupancyBean>>();
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
				request.getSession().setAttribute("loginName", staff.getName());
			}
		}
		
		return nextPage;
	}
	
	public static void displayAllBookings(HttpServletRequest request, DAO dao) {
		List<BookingDTO> allBookings = dao.getAllBookings();
		List<BookingDTO> pendingBookings = new LinkedList<BookingDTO>();
		
		for (BookingDTO booking : allBookings) {
			if (booking.getEndDate().after(Calendar.getInstance())) {
				pendingBookings.add(booking);
				System.out.println("booking: " + booking.getId());
			}
		}
		
		HashMap<String, List<BookingDTO>> results = new HashMap<String, List<BookingDTO>>(); 
		
		List<BookingDTO> booked = new LinkedList<BookingDTO>();
		List<BookingDTO> checkedin = new LinkedList<BookingDTO>();
		for (BookingDTO booking : pendingBookings) {
			List<RoomDTO> rooms = dao.getRoomsByBookingID(booking.getId());
			System.out.println(booking.getId());
			if (bookingAllCheckedIn(rooms)) {
				checkedin.add(booking);
			} else {
				booked.add(booking);
			}
		}
		results.put(Availability.CHECKEDIN.name(), checkedin);
		results.put(Availability.BOOKED.name(), booked);
		
		request.setAttribute("results", results);
		request.setAttribute("bookedNum", booked.size());
		request.setAttribute("checkedinNum", checkedin.size());
	}
	
	public static String staffSelectBooking(HttpServletRequest request, DAO dao) {
		String nextPage = "checkInPage.jsp";
		boolean checkedIn = false;
		
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
		
		if (bookingAllCheckedIn(dao.getRoomsByBookingID(bookingID)))
			checkedIn = true;
		
		List<RoomDTO> rooms = dao.getRoomsByBookingID(bookingID);
		CustomerDTO customer = dao.getCustomerByBookingID(bookingID);
				
		request.setAttribute("checkedIn", checkedIn);
		request.setAttribute("customer", customer);
		request.setAttribute("rooms", rooms);
		request.setAttribute("bookingID", bookingID);
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
				bookings.add(dao.getCustomerBookingByID(bookingID));
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
	
	public static String checkIn(HttpServletRequest request, DAO dao, PassByRef pbr) {
		String nextPage = "checkInPage.jsp";
		boolean checkedIn = false;
		
		String[] roomIDs = request.getParameterValues("checkInRooms");
		
		if (roomIDs == null) {
			displayAllBookings(request, dao);
			pbr.addErrorMessage("Select a room to check in.");
			return "staffPage.jsp";
		}
		
		List<RoomDTO> rooms = new ArrayList<RoomDTO>();
		
		for (String roomID : roomIDs) {
			try {
				int room_id = Integer.parseInt(roomID);
				dao.updateRoomAvailability(room_id, "checkedin");
				rooms.add(dao.getRoomByID(room_id));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		checkedIn = true;
		request.setAttribute("rooms", rooms);
		request.setAttribute("checkedIn", checkedIn);
		
		return nextPage;
	}
	
	public static String checkOut(HttpServletRequest request, DAO dao, PassByRef pbr) {
		String nextPage = "checkInPage.jsp";
		boolean checkedIn = false;
		
		String[] roomIDs = request.getParameterValues("checkOutRooms");
		
		if (roomIDs == null) {
			displayAllBookings(request, dao);
			pbr.addErrorMessage("Select a room to check in.");
			return "staffPage.jsp";
		}
		
		List<RoomDTO> rooms = new ArrayList<RoomDTO>();
		
		for (String roomID : roomIDs) {
			try {
				int room_id = Integer.parseInt(roomID);
				dao.updateRoomAvailability(room_id, "available");
				rooms.add(dao.getRoomByID(room_id));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		checkedIn = false;
		request.setAttribute("rooms", rooms);
		request.setAttribute("checkedIn", checkedIn);
		
		return nextPage;
	}
	
	public static void displayAllOccupancies(HttpServletRequest request, DAO dao) {
		List<HotelDTO> hotels = dao.getAllHotelLocations();
		HashMap<String, List<OccupancyBean>> results = new HashMap<String, List<OccupancyBean>>();
		
		for (HotelDTO hotel : hotels) {
			List<OccupancyBean> occupancies = dao.getRoomsOccupancyByLocation(hotel.getLocation());
			if (occupancies.size() > 0)
				results.put(hotel.getLocation(), occupancies);
		}
		
		request.setAttribute("occupancies", results);
	}
	
	public static void logout(HttpServletRequest request, DAO dao) {
		request.removeAttribute("loginName");
		request.getSession().invalidate();
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
	
	public static boolean bookingAllCheckedIn(List<RoomDTO> rooms) {
		int checkedInCnt = 0;
		for (RoomDTO room : rooms) {
			if (room.getAvailability() == Availability.BOOKED) {
				return false;
			} else if (room.getAvailability() == Availability.CHECKEDIN) {
				checkedInCnt++;
			}
		}
		
		if (checkedInCnt == rooms.size())
			return true;
		
		return false;
	}
	
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}
	
	public static boolean isValidDateRange(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		if (endYear < startYear) {
			return false;
		}
		else if (endYear == startYear) {
			if (endMonth < startMonth) {
				return false;
			}
			else if (endMonth == startMonth) {
				if (endDay < startDay) {
					return false;
				} else {
					if (endDay == startDay) {
						return true;
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isPresentFutureDate(int year, int month, int day) {
		if (year < getCurrentYear()) {
			return false;
		}
		else if (year == getCurrentYear()) {
			if (month < getCurrentMonth()) {
				return false;
			}
			else if (month == getCurrentMonth()) {
				if (day < getCurrentDay()) {
					return false;
				} else {
					if (day == getCurrentDay()) {
						return true;
					}
				}
			}
		}
		return true;
	}
}
