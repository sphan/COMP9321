package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.BookingSelection;
import edu.unsw.comp9321.bean.OccupancyBean;
import edu.unsw.comp9321.bean.SearchDetailsBean;
import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.logic.PassByRef;

public class DAO {
	//This class should be the only class to talk to database
	//Just keep adding more functions as we need them
	//TODO must add insert functions here as well

	static Logger logger = Logger.getLogger(DAO.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	private PassByRef pbr;

	public DAO (PassByRef pbr) {
		this.pbr = pbr;
		try {
			connection = DBConnectionFactory.getConnection();
		} catch (ServiceLocatorException e) {
			e.printStackTrace();
			pbr.addErrorMessage("ServiceLocatorException in DAO");
		} catch (SQLException e) {
			e.printStackTrace();
			pbr.addErrorMessage("SQLException in DAO");
		}
		logger.info("Got connection");
	}
	
	public List<HotelDTO> getAllHotelLocations() {
		List<HotelDTO> hotels = new ArrayList<HotelDTO>();
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select id, name, location from hotel";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			while (res.next()) {
				int id = res.getInt("id");
				String name = res.getString("name");
				String location = res.getString("location");
				hotels.add(new HotelDTO(id, name, location));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getAllHotelLocation");
		}
		
		return hotels;
	}
	
	public HotelDTO getHotelByRoomID(int room_id) {
		HotelDTO hotel = null;
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select h.id, h.name, h.location from hotel h " +
					"join room r on (r.hotel_id = h.id) " +
					"where r.id = " + room_id;
			ResultSet res = stmnt.executeQuery(query_cast);
			res.next();
			
			hotel = new HotelDTO(res.getInt("id"), res.getString("name"), res.getString("location"));
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getAllHotelLocation");
		}
		
		return hotel;
	}

	public List<RoomTypeDTO> getHotelRoomSelection(SearchDetailsBean sdb) {
		List<RoomTypeDTO> roomTypeList = new ArrayList<RoomTypeDTO>();

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = 
					"select "
					+ "rt.room_type, "
					+ "rt.price, "
					+ "count(rt.room_type) as count "
					+ "from "
					+ "room r "
					+ "join "
					+ "hotel h "
					+ "on (r.hotel_id=h.id) "
					+ "join "
					+ "room_type rt "
					+ "on (rt.id=r.room_type_id) "
					+ "where "
					+ "h.location='"+sdb.getLocation()+"' "
					+ "and "
					+ "rt.price <="+sdb.getMaxPrice()+" "
					+ "and "
					+ "r.id not in "
					+ "(select "
					+ "r.id "
					+ "from "
					+ "room_schedule rs "
					+ "join "
					+ "customer_booking cb "
					+ "on (rs.customer_booking_id=cb.id) "
					+ "join "
					+ "room r "
					+ "on (r.id=rs.room_id) "
					+ "join "
					+ "room_type rt "
					+ "on (rt.id=r.room_type_id) "
					+ "where "
					+ "(cb.start_date between '"+sdb.getStartYear()+"-"+sdb.getStartMonth()+"-"+sdb.getStartDay()+"' and '"+sdb.getEndYear()+"-"+sdb.getEndMonth()+"-"+sdb.getEndDay()+"') "
					+ "or "
					+ "(cb.end_date between '"+sdb.getStartYear()+"-"+sdb.getStartMonth()+"-"+sdb.getStartDay()+"' and '"+sdb.getEndYear()+"-"+sdb.getEndMonth()+"-"+sdb.getEndDay()+"')) "
					+ "group by rt.room_type, rt.price ";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while (res.next()) {
				String room_type = res.getString("room_type");
				int price = res.getInt("price");
				int count = res.getInt("count");
				roomTypeList.add(new RoomTypeDTO(room_type, price, count));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getHotelRoomTypes");
		}
		return roomTypeList;
	}
	
	/**
	 * Get all room occupancies for the hotel chain by the given location.
	 * @param location The location.
	 * @return
	 */
	public List<OccupancyBean> getRoomsOccupancyByLocation(String location) {
		List<OccupancyBean> occupancies = new ArrayList<OccupancyBean>();
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, r.availability, " +
					"count(r.availability) as count from room r " +
					"join room_type rt on (r.room_type_id=rt.id) " +
					"join hotel h on (h.id=r.hotel_id) where h.location = '" +
					location + "' group by rt.room_type, r.availability order by rt.room_type";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			while (res.next()) {
				String room_type = res.getString("room_type");
				final String availability = res.getString("availability");
				final int availNum = res.getInt("count");
				
				occupancies.add(new OccupancyBean(room_type, availability, availNum));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomsOccupancyByLocation");
		}
		
		return occupancies;
	}
	
	/**
	 * Get all room occupancies for the hotel chain by the given location.
	 * @param location The location.
	 * @return
	 */
	public List<OccupancyBean> getRoomsOccupancyByLocation(String location, String availability) {
		List<OccupancyBean> roomOccupancy = new ArrayList<OccupancyBean>();
		System.out.println(location);
		System.out.println(availability);
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, r.availability, count(r.availability) " +
					"as count from room r join room_type rt on (r.room_type_id=rt.id) " +
					"join hotel h on (h.id=r.hotel_id) where h.location = '" + location + "' " +
					"and r.availability = '" + availability + "' " +
					"group by rt.room_type, r.availability order by rt.room_type"; 
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			while (res.next()) {
				String room_type = res.getString("room_type");
				String avail = res.getString("availability");
				int availNum = res.getInt("count");
				roomOccupancy.add(new OccupancyBean(room_type, avail, availNum));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomsOccupancyByLocation");
		}
		
		return roomOccupancy;
	}


	//return all bookings including past completed ones
	public List<BookingDTO> getAllBookings() {
		List<BookingDTO> bookings = new ArrayList<BookingDTO>();
		try {
			Statement stmnt = connection.createStatement();
			String query_cast =
					"SELECT "
							+ "cb.id as cbid,"
							+ "cb.start_date,"
							+ "cb.end_date,"
							+ "c.id as cid,"
							+ "c.first_name,"
							+ "c.last_name "
							+ "FROM CUSTOMER_BOOKING cb "
							+ "JOIN CUSTOMER c "
							+ "ON (cb.CUSTOMER_ID=c.ID)";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while (res.next()) {
				bookings.add(rebuildBooking(res));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getAllBookings");
		}
		return bookings;
	}

	/**
	 * Return booking by the given booking id.
	 * @param bid
	 * @return
	 */
	public BookingDTO getBookingByID(int bid) {
		BookingDTO booking = null;

		try {
			Statement stmnt = connection.createStatement();
			String query_cast =	"SELECT "
					+ "cb.id as cbid,"
					+ "cb.start_date,"
					+ "cb.end_date,"
					+ "c.id as cid,"
					+ "c.first_name,"
					+ "c.last_name "
					+ "FROM CUSTOMER_BOOKING cb "
					+ "JOIN CUSTOMER c "
					+ "ON (cb.CUSTOMER_ID=c.ID)"
					+ "WHERE cb.id = " + bid;
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+ res.getFetchSize());
			while (res.next()) {
				booking = rebuildBooking(res);
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getBookingsByID");
		}

		return booking;
	}

	public List<BookingDTO> getBookingsByCustomerName(String customerName) {
		List<BookingDTO> bookings = new ArrayList<BookingDTO>();

		try {
			Statement stmnt = connection.createStatement();
			String query_cast =	"SELECT "
					+ "cb.id as cbid,"
					+ "cb.start_date,"
					+ "cb.end_date,"
					+ "c.id as cid,"
					+ "c.first_name,"
					+ "c.lastname,"
					+ "FROM CUSTOMER_BOOKING cb "
					+ "JOIN CUSTOMER c "
					+ "ON (cb.CUSTOMER_ID=c.ID)"
					+ "WHERE c.first_name = '" + customerName + "'";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+ res.getFetchSize());
			while (res.next()) {
				bookings.add(rebuildBooking(res));
				System.out.println("getting booking");
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getBookingsByCustomerName");
		}

		return bookings;
	}

	public BookingDTO addBooking(
			int custID, int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear, BookingListBean blb) {
		BookingDTO returnRes = null;
		System.out.println("add booking");
		if (!isValidDate(startDay, startMonth, startYear) || !isValidDate(endDay, endMonth, endYear)) {
			return returnRes;
		}
		try {
			PreparedStatement preppedStmnt = connection.prepareStatement(
					"INSERT "
							+ "INTO "
							+ "CUSTOMER_BOOKING "
							+ "VALUES(DEFAULT,?,?,?)", 
							Statement.RETURN_GENERATED_KEYS);
			preppedStmnt.setInt(1, custID);
			preppedStmnt.setString(2, startYear+"-"+startMonth+"-"+startDay);
			preppedStmnt.setString(3, endYear+"-"+endMonth+"-"+endDay);
			preppedStmnt.executeUpdate();

			ResultSet res = preppedStmnt.getGeneratedKeys();
			int last_inserted_id = 0;
			if (res.next()) {
				last_inserted_id = res.getInt(1);
			}
			Statement stmnt = connection.createStatement();
			String query_cast = 
					"SELECT "
							+ "cb.id as cbid,"
							+ "cb.start_date,"
							+ "cb.end_date,"
							+ "c.id as cid,"
							+ "c.first_name,"
							+ "c.last_name "
							+ " FROM CUSTOMER_BOOKING CB "
							+ "JOIN CUSTOMER C "
							+ "ON (CB.CUSTOMER_ID=C.ID) "
							+ "WHERE CB.ID="+last_inserted_id;
			res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			while (res.next()) {
				returnRes = rebuildBooking(res);
			}
			//allocate rooms to the booking from bookingList
			List<BookingSelection> bsList = blb.getList();
			for (BookingSelection bs : bsList) {
				stmnt = connection.createStatement();
				query_cast = "select "
						+ "r.id "
						+ "from "
						+ "room r "
						+ "join "
						+ "hotel h "
						+ "on (r.hotel_id=h.id) "
						+ "join "
						+ "room_type rt "
						+ "on (rt.id=r.room_type_id) "
						+ "where h.location='"+blb.getLocation()+"' "
						+ "and "
						+ "rt.room_type='"+bs.getRoomType()+"' "
						+ "and "
						+ "r.id "
						+ "not in "
						+ "(select "
						+ "r.id "
						+ "from "
						+ "room_schedule rs "
						+ "join "
						+ "customer_booking cb "
						+ "on (rs.customer_booking_id=cb.id) "
						+ "join room r "
						+ "on (r.id=rs.room_id) "
						+ "where "
						+ "(cb.start_date between '"+blb.getStartYear()+"-"+blb.getStartMonth()+"-"+blb.getStartDay()+"' and '"+blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay()+"') "
						+ "or "
						+ "(cb.end_date between '"+blb.getStartYear()+"-"+blb.getStartMonth()+"-"+blb.getStartDay()+"' and '"+blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay()+"'))";
				res = stmnt.executeQuery(query_cast);
				logger.info("The result set size is "+res.getFetchSize());
				if (res.next()) {
					preppedStmnt = 
							connection.prepareStatement(""
									+ "INSERT "
									+ "INTO "
									+ "ROOM_SCHEDULE "
									+ "VALUES(DEFAULT,?,?)", 
									Statement.RETURN_GENERATED_KEYS);
					preppedStmnt.setString(1, String.valueOf(res.getInt("id")));
					System.out.println(res.getInt("id"));
					preppedStmnt.setString(2, String.valueOf(last_inserted_id));
					System.out.println(last_inserted_id);
					preppedStmnt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnRes;
	}
	
	public void updateRoomAvailability(int room_id, String new_avail) {
		try {
			PreparedStatement pStatement = connection.prepareStatement(
					"update room set availability = ? where id = ?");
			pStatement.setString(1, new_avail);
			pStatement.setInt(2, room_id);
			pStatement.executeUpdate();
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in updateRoomAvailability");
		}
	}

	//return every single rooms, plan to use this for room occupancy
	public List<RoomDTO> getAllRooms() {
		List<RoomDTO> rooms = new ArrayList<RoomDTO>();
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM ROOM";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			while (res.next()) {
				int id = res.getInt("id");
				int room_number = res.getInt("room_number");
				float price = res.getFloat("price");
				float discounted_price = res.getFloat("discounted_price");
				String room_type = res.getString("room_type").toUpperCase();
				String availability = res.getString("availability");
				int hotel = res.getInt("hotel");
				rooms.add(new RoomDTO(id, room_number, price, discounted_price, room_type, availability, hotel));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rooms;
	}
	
	public List<RoomDTO> getRoomsByBooking(int bookingID) {
		List<RoomDTO> rooms = new ArrayList<RoomDTO>();
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select r.id as room_id, r.room_number, r.availability, " +
					"rt.room_type, h.id as hotel_id, h.location from room r join room_type rt " +
					"on (rt.id = r.room_type_id) " +
					"join hotel h on (h.id = r.hotel_id) " +
					"join room_schedule rs on (rs.room_id = r.id) " +
					"where rs.customer_booking_id = " + bookingID;
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is " + res.getFetchSize());
			
			while (res.next()) {
				int id = res.getInt("room_id");
				int roomNum = res.getInt("room_number");
				String availability = res.getString("availability");
				String room_type = res.getString("room_type");
				int hotel = res.getInt("hotel_id");
//				String location = res.getString("location");
				
				rooms.add(new RoomDTO(id, roomNum, 0, 0, room_type, availability, hotel));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rooms;
	}
	
	public RoomDTO getRoomByID(int room_id) {
		RoomDTO room = null;
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select r.id, r.room_number, r.availability, rt.room_type, r.hotel_id " +
					"from room r join room_type rt on (rt.id = r.room_type_id)" +
					"where r.id = " + room_id;
			ResultSet res = stmnt.executeQuery(query_cast);
			res.next();
			
			int id = res.getInt("id");
			int roomNum = res.getInt("room_number");
			String availability = res.getString("availability");
			String room_type = res.getString("room_type");
			int hotel = res.getInt("hotel_id");
			
			room = new RoomDTO(id, roomNum, 0, 0, room_type, availability, hotel);
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomByID");
		}
		
		return room;
	}

	public CustomerDTO addCustomer(String firstName, String lastName) {
		firstName = firstName.toUpperCase();
		lastName = lastName.toUpperCase();
		CustomerDTO returnResult = null;
		try {
			PreparedStatement preppedStmnt = 
					connection.prepareStatement(""
							+ "INSERT "
							+ "INTO "
							+ "CUSTOMER "
							+ "VALUES(DEFAULT,?,?)", 
							Statement.RETURN_GENERATED_KEYS);
			preppedStmnt.setString(1, firstName);
			preppedStmnt.setString(2, lastName);
			preppedStmnt.executeUpdate();
			//added to db
			//now get from db and return;
			ResultSet res = preppedStmnt.getGeneratedKeys();
			int last_inserted_id = 0;
			if (res.next()) {
				last_inserted_id = res.getInt(1);
			}
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM CUSTOMER WHERE ID="+last_inserted_id;
			res = stmnt.executeQuery(query_cast);
			while (res.next()) {
				int sqlid = res.getInt("id");
				String sqlFirstName = res.getString("first_name");
				String sqlLastName = res.getString("last_name");

				returnResult = new CustomerDTO(sqlid, sqlFirstName, sqlLastName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnResult;
	}

	//return all customers, unsure if we need lists of customers
	public List<CustomerDTO> getAllCustomers() {
		List<CustomerDTO> customers = new ArrayList<CustomerDTO>(); 

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM CUSTOMER";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			while (res.next()) {
				int id = res.getInt("id");
				String firstName = res.getString("first_name");
				String lastName = res.getString("last_name");
				customers.add(new CustomerDTO(id, firstName, lastName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customers;
	}
	
	public CustomerDTO getCustomerByBookingID(int bookingID) {
		CustomerDTO customer = null;
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select c.id, c.first_name, c.last_name " +
					"from customer c " +
					"join customer_booking cb on " +
					"(cb.customer_id = c.id) " +
					"where cb.id = " + bookingID;
			System.out.println(query_cast);
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			int id = res.getInt("id");
			String first_name = res.getString("first_name");
			String last_name = res.getString("last_name");
			
			customer = new CustomerDTO(id, first_name, last_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return customer;
		
	}


	//return all staff in hotels
	public List<StaffDTO> getAllStaff() {
		List<StaffDTO> staff = new ArrayList<StaffDTO>(); 

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM STAFF";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			while (res.next()) {
				System.out.println("test");
				int id = res.getInt("id");
				String name = res.getString("name");
				String userName = res.getString("username");
				String password = res.getString("password");
				staff.add(new StaffDTO(id, name, userName, password));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return staff;
	}

	/**
	 * Get staff by the given username.
	 * @param username The staff username.
	 * @return The staff.
	 */
	public StaffDTO getStaffByUsername(String username) {
		StaffDTO staff = null;
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT id, name, username, password, staff_type FROM STAFF WHERE username = '" + username + "'";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			res.next();
			int id = res.getInt("id");
			String name = res.getString("name");
			String usr = res.getString("username");
			String pw = res.getString("password");
			staff = new StaffDTO(id, name, usr, pw);
			staff.setType(res.getString("staff_type"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staff;
	}

	//returns list of all hotels
	public List<HotelDTO>getAllHotels() {
		List<HotelDTO>hotels = new ArrayList<HotelDTO>();
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM HOTEL";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());

			while (res.next()) {
				int id = res.getInt("id");
				String name = res.getString("name");
				String location = res.getString("location");

				hotels.add(new HotelDTO(id, name, location));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hotels;
	}

	private BookingDTO rebuildBooking(ResultSet res) throws SQLException {
		int c_id = res.getInt("cid");
		String c_Firstname = res.getString("first_name");
		String c_LastName = res.getString("last_name");

		int cb_id = res.getInt("cbid");
		Calendar startDate = new GregorianCalendar();
		startDate.setTime(res.getDate("start_date"));
		Calendar endDate = new GregorianCalendar();
		endDate.setTime(res.getDate("end_date"));
		
		return new BookingDTO(cb_id, new CustomerDTO(c_id, c_Firstname, c_LastName), startDate, endDate);
	}

	private boolean isValidDate(int day, int month, int year) {
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

}
