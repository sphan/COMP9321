package edu.unsw.comp9321.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import sun.security.action.GetBooleanAction;

import edu.unsw.comp9321.bean.BookingRoomDetailBean;
import edu.unsw.comp9321.bean.OccupancyBean;
import edu.unsw.comp9321.bean.OwnerPriceBean;
import edu.unsw.comp9321.jdbc.Availability;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.CustomerDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.HotelDTO;
import edu.unsw.comp9321.jdbc.RoomDTO;
import edu.unsw.comp9321.jdbc.RoomScheduleDTO;
import edu.unsw.comp9321.jdbc.StaffDTO;
import edu.unsw.comp9321.jdbc.StaffType;

public class Command {
	public static String ownerSearch(HttpServletRequest request, DAO dao) {
		String nextPage = "ownerPage.jsp";
		
		String hotelLocation = request.getParameter("hotelLocation");
		String roomAvail = request.getParameter("roomAvail").toLowerCase().replaceAll("\\W", "");
		HashMap<String, List<OccupancyBean>> results = new HashMap<String, List<OccupancyBean>>();
		
		List<OccupancyBean> occupancies = null;
		
		if (hotelLocation.isEmpty()) {
			List<HotelDTO> hotels = dao.getAllHotelLocations();
			if (roomAvail.equalsIgnoreCase("all")) {
				for (HotelDTO hotel : hotels) {
					occupancies = dao.getRoomsOccupancyByLocation(hotel.getLocation());
					results.put(hotel.getLocation(), occupancies);
				}
			} else {
				for (HotelDTO hotel : hotels) {
					occupancies = dao.getRoomsOccupancyByLocation(hotel.getLocation(), roomAvail);
					if (!occupancies.isEmpty())
						results.put(hotel.getLocation(), occupancies);
				}
			}
			request.setAttribute("occupancies", results);
			return nextPage;
		}
		
		if (roomAvail.equalsIgnoreCase("all")) {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation);
		} else {
			occupancies = dao.getRoomsOccupancyByLocation(hotelLocation, roomAvail);
		}
		
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
			}
		}
		
		HashMap<String, List<BookingDTO>> results = new HashMap<String, List<BookingDTO>>(); 
		
		List<BookingDTO> booked = new LinkedList<BookingDTO>();
		List<BookingDTO> checkedin = new LinkedList<BookingDTO>();
		for (BookingDTO booking : pendingBookings) {
			List<BookingRoomDetailBean> roomDetails = dao.getBookingRoomDetails(booking.getId());
			if (!bookingAllCheckedIn(roomDetails)) {
				booked.add(booking);
			} else if (!bookingCompleted(roomDetails) && bookingAllCheckedIn(roomDetails)) {
				checkedin.add(booking);
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
		
		List<BookingRoomDetailBean> requestedRooms = dao.getBookingRoomDetails(bookingID);
		HashMap<String, Integer> roomCount = requestRoomCount(requestedRooms);
		
		if (bookingAllCheckedIn(requestedRooms))
			checkedIn = true;
		
		List<BookingRoomDetailBean> availableRooms = new ArrayList<BookingRoomDetailBean>();
		
		if (checkedIn) {
			List<RoomScheduleDTO> rooms = dao.getRoomScheduleByCustomerBookingID(bookingID);
			for (RoomScheduleDTO room : rooms) {
				availableRooms.add(new BookingRoomDetailBean(room.getRoomType(), dao.getHotelByID(room.getRoom().getHotel()), room.getRoom()));
			}
		} else {
			availableRooms = dao.getAvailableRooms(bookingID);
		}
		CustomerDTO customer = dao.getCustomerByBookingID(bookingID);
				
		request.setAttribute("checkedIn", checkedIn);
		request.setAttribute("customer", customer);
		request.setAttribute("requestedRooms", roomCount);
		request.setAttribute("availableRooms", availableRooms);
		request.getSession().setAttribute("bookingID", bookingID);
		return nextPage;
	}
	
	public static String staffSearch(HttpServletRequest request, DAO dao) {
		String nextPage = "staffPage.jsp";
		String searchType = request.getParameter("search-type");
		String searchString = request.getParameter("searchString");
		List<BookingDTO> bookings = new LinkedList<BookingDTO>();
		
		if (searchString.isEmpty()) {
			displayAllBookings(request, dao);
			return nextPage;
		}
		
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
		int bookingID = (Integer) request.getSession().getAttribute("bookingID");
		List<BookingRoomDetailBean> requestDetails = dao.getBookingRoomDetails(bookingID);
		
		if (roomIDs == null || roomIDs.length > requestDetails.size() ||
			!correctRoomsSelected(requestDetails, roomIDs, dao)) {
			List<BookingRoomDetailBean> availableRooms = dao.getAvailableRooms(bookingID);
			CustomerDTO customer = dao.getCustomerByBookingID(bookingID);
					
			request.setAttribute("checkedIn", checkedIn);
			request.setAttribute("customer", customer);
			request.setAttribute("requestedRooms", requestRoomCount(dao.getBookingRoomDetails(bookingID)));
			request.setAttribute("availableRooms", availableRooms);
			request.getSession().setAttribute("bookingID", bookingID);
			return nextPage;
		}
		
		List<BookingRoomDetailBean> rooms = new ArrayList<BookingRoomDetailBean>();
		
		for (String roomID : roomIDs) {
			try {
				int room_id = Integer.parseInt(roomID);
				dao.updateRoomAvailability(room_id, "checkedin");
				RoomDTO room = dao.getRoomByID(room_id);
				dao.updateRoomSchedule(bookingID, room_id, room.getRoomType().name());
				rooms.add(new BookingRoomDetailBean(room.getRoomType().name(), dao.getHotelByID(room.getHotel()), room));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		checkedIn = true;
		request.setAttribute("availableRooms", rooms);
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
		
		List<BookingRoomDetailBean> rooms = new ArrayList<BookingRoomDetailBean>();
		
		for (String roomID : roomIDs) {
			try {
				int room_id = Integer.parseInt(roomID);
				dao.updateRoomAvailability(room_id, "available");
				RoomDTO room = dao.getRoomByID(room_id);
				rooms.add(new BookingRoomDetailBean(room.getRoomType().name(), dao.getHotelByID(room.getHotel()), room));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		checkedIn = false;
		request.setAttribute("availableRooms", rooms);
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
	
	public static void displayAllRoomPrices(HttpServletRequest request, DAO dao) {
		List<HotelDTO> hotels = dao.getAllHotelLocations();
		HashMap<String, List<OwnerPriceBean>> allPrices = new HashMap<String, List<OwnerPriceBean>>();
		
		for (HotelDTO hotel : hotels) {
			List<OwnerPriceBean> original = getRoomPrices(hotel.getLocation(), dao);
			allPrices.put(hotel.getLocation(), original);
		}
		
		request.setAttribute("roomPrices", allPrices);
	}
	
	public static String staffSearchRoomPrice(HttpServletRequest request, DAO dao) {
		String nextPage = "viewDiscountPrice.jsp";
		
		String location = request.getParameter("hotelLocation");
		String roomType = request.getParameter("roomType");
		
		HashMap<String, List<OwnerPriceBean>> allPrices = new HashMap<String, List<OwnerPriceBean>>();
		
		List<OwnerPriceBean> original = getRoomPrices(location, dao);
		
		if (location.isEmpty()) {
			List<HotelDTO> hotels = dao.getAllHotelLocations();
			for (HotelDTO hotel : hotels) {
				original = getRoomPrices(hotel.getLocation(), dao);
				if (!roomType.equalsIgnoreCase("all")) {
					for (OwnerPriceBean o : original) {
						if (o.getRoomType().equalsIgnoreCase(roomType)) {
							List<OwnerPriceBean> result = new ArrayList<OwnerPriceBean>();
							result.add(o);
							allPrices.put(o.getLocation(), result);
						}
					}
				} else {
					allPrices.put(hotel.getLocation(), original);
				}
			}
		} else if (roomType.equalsIgnoreCase("all")) {
			allPrices.put(location, original);
		} else {
			 
			for (OwnerPriceBean o : original) {
				if (o.getRoomType().equalsIgnoreCase(roomType)) {
					List<OwnerPriceBean> result = new ArrayList<OwnerPriceBean>();
					result.add(o);
					allPrices.put(o.getLocation(), result);
				}
			}
		}
		
		request.setAttribute("roomPrices", allPrices);
		
		return nextPage;
	}
	
	public static String displayDiscountForm(HttpServletRequest request, DAO dao) {
		String nextPage = "setDiscountPrice.jsp";
		String[] roomTypeLocation = request.getParameter("roomTypeLocation").split("-");
		
		String location = roomTypeLocation[1];
		String room_type = roomTypeLocation[0];
		
		request.setAttribute("location", location);
		request.setAttribute("roomType", room_type);
		request.setAttribute("startDate", getCurrentDay());
		request.setAttribute("startMonth", getCurrentMonth());
		request.setAttribute("startYear", getCurrentYear());
		request.setAttribute("endDate", getCurrentDayPlus());
		request.setAttribute("endMonth", getCurrentMonthPlus());
		request.setAttribute("endYear", getCurrentYearPlus());
		request.getSession().setAttribute("setDiscountStatus", "displayForm");
		
		OwnerPriceBean defaultPrice = dao.getDefaultRoomPrice(location, room_type);
		request.setAttribute("curPrice", defaultPrice.getCurrentPrice());
		return nextPage;
	}
	
	public static String setDiscountPrice(HttpServletRequest request, DAO dao) {
		String nextPage = "setDiscountPrice.jsp";
		
		String hotelLocation = request.getParameter("location");
		String roomType = request.getParameter("roomType");
		String discountPrice = request.getParameter("discountPrice");
		
		try {
			if (discountPrice.isEmpty()) {
				dao.getPbr().addErrorMessage("Invalid input.");
				presetDiscountForm(request, getCurrentDay(), getCurrentMonth(), getCurrentYear(),
						getCurrentDay() + 1, getCurrentMonth(), getCurrentYear());
				return nextPage;
			}
			int d_price = Integer.parseInt(discountPrice);
			
			if ( d_price > Integer.parseInt(request.getParameter("curPrice"))) {
				dao.getPbr().addErrorMessage("Invalid input.");
				presetDiscountForm(request, getCurrentDay(), getCurrentMonth(), getCurrentYear(),
						getCurrentDay() + 1, getCurrentMonth(), getCurrentYear());
				return nextPage;
			}
			
			int startDay = Integer.parseInt(request.getParameter("startday"));
			int startMonth = Integer.parseInt(request.getParameter("startmonth"));
			int startYear = Integer.parseInt(request.getParameter("startyear"));
			int endDay = Integer.parseInt(request.getParameter("endday"));
			int endMonth = Integer.parseInt(request.getParameter("endmonth"));
			int endYear = Integer.parseInt(request.getParameter("endyear"));
			
			if (!Command.isPresentFutureDate(startYear, startMonth, startDay) || 
				!Command.isPresentFutureDate(endYear,  endMonth,  endDay)) {
				dao.getPbr().addErrorMessage("You cannot book in the past");
				presetDiscountForm(request, getCurrentDay(), getCurrentMonth(), getCurrentYear(),
						getCurrentDay() + 1, getCurrentMonth(), getCurrentYear());
			} else if (!Command.isValidDateRange(startYear, startMonth, startDay, endYear, endMonth, endDay)) {
				dao.getPbr().addErrorMessage("Date range is invalid");
				presetDiscountForm(request, getCurrentDay(), getCurrentMonth(), getCurrentYear(),
						getCurrentDay() + 1, getCurrentMonth(), getCurrentYear());
			} else {
				String start_date = startYear + "-" + startMonth + "-" + startDay;
				String end_date = endYear + "-" + endMonth + "-" + endDay;
				
				request.setAttribute("location", hotelLocation);
				request.setAttribute("roomType", roomType);
				request.setAttribute("curPrice", request.getParameter("curPrice"));
				request.setAttribute("discountPrice", discountPrice);
				request.setAttribute("startDate", start_date);
				request.setAttribute("endDate", end_date);
				request.getSession().setAttribute("setDiscountStatus", "confirm");
				
			}
		} catch (Exception e) {
			dao.getPbr().addErrorMessage("Invalid input.");
			presetDiscountForm(request, getCurrentDay(), getCurrentMonth(), getCurrentYear(),
					getCurrentDay() + 1, getCurrentMonth(), getCurrentYear());
			e.printStackTrace();
		}
		
		return nextPage;
	}
	
	public static String backToDiscountForm(HttpServletRequest request, DAO dao) {
		String nextPage = "setDiscountPrice.jsp";
		
		String[] startDate = request.getParameter("startDate").split("-");
		String[] endDate = request.getParameter("endDate").split("-");
		
		presetDiscountForm(request, Integer.parseInt(startDate[2]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[0]),
				Integer.parseInt(endDate[2]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[0]));
		
		request.getSession().setAttribute("setDiscountStatus", "displayForm");
		
		return nextPage;
	}
	
	public static String confirmDiscountPrice(HttpServletRequest request, DAO dao) {
		String hotelLocation = request.getParameter("location");
		String roomType = request.getParameter("roomType");
		String curPrice = request.getParameter("curPrice");
		String discountPrice = request.getParameter("discountPrice");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		OwnerPriceBean priceBean = new OwnerPriceBean(Integer.parseInt(curPrice), roomType, Integer.parseInt(discountPrice),
				startDate, endDate, hotelLocation);
		dao.addDiscount(priceBean);
		
		HashMap<String, List<OwnerPriceBean>> allPrices = new HashMap<String, List<OwnerPriceBean>>();
		
		List<OwnerPriceBean> original = getRoomPrices(hotelLocation, dao);
		allPrices.put(hotelLocation, original);
		request.setAttribute("roomPrices", allPrices);
		
		return "viewDiscountPrice.jsp";
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
	
	public static boolean bookingAllCheckedIn(List<BookingRoomDetailBean> roomDetails) {
		int checkedInCnt = 0;
		
		for (BookingRoomDetailBean rd : roomDetails) {
			if (rd.getRoom() != null) {
				checkedInCnt++;
			}
		}
		
		if (checkedInCnt == roomDetails.size())
			return true;
		
		return false;
	}
	
	public static boolean bookingCompleted(List<BookingRoomDetailBean> roomDetails) {
		int checkedOutCnt = 0;
		if (bookingAllCheckedIn(roomDetails)) {
			for (BookingRoomDetailBean rd : roomDetails) {
				if (rd.getRoom().getAvailability() == Availability.AVAILABLE)
					checkedOutCnt++;
			}
		}
		if (checkedOutCnt == roomDetails.size()) {
			System.out.println("checkedOutCnt: " + checkedOutCnt);
			return true;
		}
		
		return false;
	}
	
	public static boolean correctRoomsSelected(List<BookingRoomDetailBean> roomDetails, String[] roomIDs, DAO dao) {
		HashMap<String, Integer> roomTypeCount = requestRoomCount(roomDetails);
		int zeroCount = 0;
		
		for (String room_id : roomIDs) {
			int id = Integer.parseInt(room_id);
			RoomDTO room = dao.getRoomByID(id);
			roomTypeCount.put(room.getRoomType().name(), roomTypeCount.get(room.getRoomType().name()) - 1);
			if (roomTypeCount.get(room.getRoomType().name()) < 0)
				return false;
		}
		
		for (String roomType : roomTypeCount.keySet()) {
			if (roomTypeCount.get(roomType) == 0)
				zeroCount++;
		}
		
		if (zeroCount == roomTypeCount.size())
			return true;
		
		return false;
	}
	
	public static HashMap<String, Integer> requestRoomCount(List<BookingRoomDetailBean> roomDetails) {
		HashMap<String, Integer> roomTypeCount = new HashMap<String, Integer>();
		for (BookingRoomDetailBean rd : roomDetails) {
			if (roomTypeCount.containsKey(rd.getRoomType())) {
				roomTypeCount.put(rd.getRoomType(), roomTypeCount.get(rd.getRoomType()) + 1);
			} else {
				roomTypeCount.put(rd.getRoomType(), 1);
			}
		}
		
		return roomTypeCount;
	}
	
	public static List<OwnerPriceBean> getRoomPrices(String location, DAO dao) {
		List<OwnerPriceBean> discounted = dao.getRoomPrices(location, Command.getCurrentDay(),
				Command.getCurrentMonth(), Command.getCurrentYear());
		List<OwnerPriceBean> original = dao.getRoomPrices(location);
		for (OwnerPriceBean p : discounted) {
			original.add(p);
		}
		
		return original;
	}
	
	public static void presetDiscountForm(HttpServletRequest request, int startday, int startmonth, int startyear,
			int endday, int endmonth, int endyear) {
		request.setAttribute("location", request.getParameter("location"));
		request.setAttribute("roomType", request.getParameter("roomType"));
		request.setAttribute("curPrice", request.getParameter("curPrice"));
		request.setAttribute("discountPrice", request.getParameter("discountPrice"));
		request.setAttribute("startDate", startday);
		request.setAttribute("startMonth", startmonth);
		request.setAttribute("startYear", startyear);
		request.setAttribute("endDate", endday);
		request.setAttribute("endMonth", endmonth);
		request.setAttribute("endYear", endyear);
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
	
	public static int getCurrentDayPlus() {
		if (!isValidDate(getCurrentDay()+1, getCurrentMonth(), getCurrentYear())) {
			return 1;
		}
		return getCurrentDay()+1;
	}

	public static int getCurrentMonthPlus() {
		if (!isValidDate(getCurrentDay()+1, getCurrentMonth(), getCurrentYear())) {
			if (!isValidDate(1, getCurrentMonth()+1, getCurrentYear())) {
				if (!isValidDate(1, 1, getCurrentYear()+1)) {
					return 0;
				}
				return 1;
			}
			return getCurrentMonth()+1;
		}
		return getCurrentMonth();
	}

	
	public static int getCurrentYearPlus() {
		if (!isValidDate(getCurrentDay()+1, getCurrentMonth(), getCurrentYear())) {
			if (!isValidDate(1, getCurrentMonth()+1, getCurrentYear())) {
				if (!isValidDate(1, 1, getCurrentYear()+1)) {
					return 0;
				}
				return getCurrentYear() + 1;
			}
		}
		return getCurrentYear();
	}

	
	private static boolean isValidDate(int day, int month, int year) {
		if (month==4||month==6||month==9||month==11) {
			if (day<1||day>30) {
				return false;
			} else {
				return true;
			}
		} else if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
			return true;
		} else if (month==2) {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				if (day<1||day>29) {
					return false;
				} else {
					return true;
				}
			} else {
				if (day<1||day>28) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			return false;
		}
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
