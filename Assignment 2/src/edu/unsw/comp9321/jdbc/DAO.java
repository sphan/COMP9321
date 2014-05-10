package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.BookingSelection;
import edu.unsw.comp9321.bean.OccupancyBean;
import edu.unsw.comp9321.bean.OwnerPriceBean;
import edu.unsw.comp9321.bean.SearchDetailsBean;
import edu.unsw.comp9321.exception.EmptyResultException;
import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.logic.Command;
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
		PreparedStatement ps = null;
		ResultSet allSelection = null;
		ResultSet bookedSelection = null;

		try {
			ps = connection.prepareStatement(
					"select "
							+ "rt.room_type, "
							+ "rt.price, "
							+ "count(rt.room_type) as count "
							+ "from "
							+ "room_type rt "
							+ "join "
							+ "room r "
							+ "on "
							+ "(r.room_type_id=rt.id) "
							+ "join "
							+ "hotel h "
							+ "on "
							+ "(h.id=r.hotel_id) "
							+ "where "
							+ "h.location=? "
							+ "and "
							+ "rt.price<=? "
							+ "group by "
							+ "rt.room_type, "
							+ "rt.price");
			ps.setString(1, sdb.getLocation());
			ps.setInt(2, sdb.getMaxPrice());
			allSelection = ps.executeQuery();

			ps = connection.prepareStatement(
					"select "
							+ "rt2.room_type, "
							+ "rt2.price, "
							+ "count(rt2.room_type) as count "
							+ "from room_schedule rs "
							+ "join "
							+ "room_type rt2 "
							+ "on "
							+ "(rs.room_type_id=rt2.id) "
							+ "join "
							+ "customer_booking cb "
							+ "on "
							+ "(cb.id=rs.customer_booking_id) "
							+ "join "
							+ "hotel h "
							+ "on "
							+ "(cb.hotel_id=h.id) "
							+ "where "
							+ "(h.location=?) "
							+ "or "
							+ "((cb.start_date between ? and ?) "
							+ "or "
							+ "(cb.end_date between ? and ?)) "
							+ "group by "
							+ "rt2.room_type, "
							+ "rt2.price");
			ps.setString(1, sdb.getLocation());
			ps.setString(2, sdb.getStartYear()+"-"+sdb.getStartMonth()+"-"+sdb.getStartDay());
			ps.setString(3, sdb.getEndYear()+"-"+sdb.getEndMonth()+"-"+sdb.getEndDay());
			ps.setString(4, sdb.getStartYear()+"-"+sdb.getStartMonth()+"-"+sdb.getStartDay());
			ps.setString(5, sdb.getEndYear()+"-"+sdb.getEndMonth()+"-"+sdb.getEndDay());
			bookedSelection = ps.executeQuery();
			Map<String, Integer> allSelectCount = new HashMap<String,Integer>();
			Map<String, Integer> allSelectPrice = new HashMap<String,Integer>();
			while (allSelection.next()) {
				allSelectCount.put(allSelection.getString("room_type"), allSelection.getInt("count"));
				allSelectPrice.put(allSelection.getString("room_type"), allSelection.getInt("price"));
			}
			while (bookedSelection.next()) {
				if (allSelectCount.containsKey(bookedSelection.getString("room_type"))) {
					//find matching room_Type and subtract count
					allSelectCount.put(bookedSelection.getString("room_type"), (allSelectCount.get(bookedSelection.getString("room_type")) - bookedSelection.getInt("count")));
				}
			}
			Iterator<String> iter = allSelectCount.keySet().iterator();
			while (iter.hasNext()) {
				String roomType = (String)iter.next();
				int price = allSelectPrice.get(roomType);
				int count = allSelectCount.get(roomType);
				roomTypeList.add(new RoomTypeDTO(roomType, price, count));
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @param customerBookingID
	 * @return
	 */
	public BookingDTO getCustomerBookingByID(int customerBookingID) {
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
					+ "WHERE cb.id = " + customerBookingID;
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

	public int getRoomTypeIDByName(String roomType) {
		PreparedStatement ps = null;
		ResultSet result = null;
		int id = 0;
		try {
			ps = connection.prepareStatement("SELECT id FROM ROOM_TYPE WHERE room_type=?");
			ps.setString(1, roomType);
			result = ps.executeQuery();
			if (result.next()) {
				id = result.getInt("id");
			} else {
				throw new EmptyResultException();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public void addRoomSchedule(int custBookingID, String roomType, String location, String startDate, String endDate) {
		PreparedStatement ps = null;
		ResultSet result = null;
		ResultSet generatedKeys = null;

		try {
			ps = connection.prepareStatement("INSERT INTO ROOM_SCHEDULE VALUES(DEFAULT, null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, custBookingID);
			ps.setInt(2, getRoomTypeIDByName(roomType));
			ps.executeUpdate();

			generatedKeys = ps.getGeneratedKeys();
			if (!generatedKeys.next()) {
				throw new EmptyResultException();
			}
		} catch (Exception e) {

		}
	}

	public BookingDTO addCustomerBooking(int custID, BookingListBean blb) {
		BookingDTO booking = null;
		ResultSet generatedKeys = null;
		List<HotelDTO> hotels = getAllHotels();
		HotelDTO hotel = null;
		for (HotelDTO h : hotels) {
			//find the current hotel
			if (h.getLocation().equals(blb.getLocation())) {
				hotel = h;
			}
		}
		try {


			PreparedStatement ps = connection.prepareStatement("INSERT INTO CUSTOMER_BOOKING VALUES(DEFAULT,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, custID);
			ps.setString(2, blb.getStartYear()+"-"+blb.getStartMonth()+"-"+blb.getStartDay());
			ps.setString(3, blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay());
			ps.setInt(4, hotel.getId());
			ps.executeUpdate();//create booking entry
			generatedKeys = ps.getGeneratedKeys();

			if (generatedKeys.next()) {
				int customerBookingID = generatedKeys.getInt(1);
				//now that entry has been made, make room_schedule entries
				for (BookingSelection bs : blb.getList()) {
					addRoomSchedule(customerBookingID, 
							bs.getRoomType(), 
							blb.getLocation(), 
							blb.getStartYear()+"-"+blb.getStartMonth()+"-"+blb.getStartDay(), 
							blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay()
							);
				}
				//after adding room schedules, getting booking would include room schedules
				booking = getCustomerBookingByID(customerBookingID);
			} else {
				throw new SQLException("creating new booking failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return booking;
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

	public List<RoomDTO> getRoomsByBookingID(int bookingID) {
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

				rooms.add(new RoomDTO(id, roomNum, room_type, availability, hotel));
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

			if (res.next()){
				int id = res.getInt("id");
				int roomNum = res.getInt("room_number");
				String availability = res.getString("availability");
				String room_type = res.getString("room_type");
				int hotel = res.getInt("hotel_id");

				room = new RoomDTO(id, roomNum, room_type, availability, hotel);
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomByID");
		}

		return room;
	}

	public List<OwnerPriceBean> getRoomPrices(String location) {
		List<OwnerPriceBean> roomPrices = new ArrayList<OwnerPriceBean>();
		int today_day = Command.getCurrentDay();
		int today_month = Command.getCurrentMonth();
		int today_year = Command.getCurrentYear();

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, rt.price from room_type rt " +
					"join room r on (r.room_type_id = rt.id) " +
					"join hotel h on (h.id = r.hotel_id) " +
					"where h.location = '" + location + "' " +
					"and rt.id not in " +
					"(select rt2.id from room_type rt2 " +
					"join discount d on (d.room_type_id = rt2.id) " +
					"join hotel h on (h.id = d.hotel_id) " +
					"where h.location = '" + location + "' " +
					"and '" + today_year + "-" + today_month + "-" + today_day + "' " +
					"between d.start_date and d.end_date) " +
					"group by rt.room_type, rt.price";
			ResultSet res = stmnt.executeQuery(query_cast);

			while (res.next()) {
				String room_type = res.getString("room_type");
				int price = res.getInt("price");
				roomPrices.add(new OwnerPriceBean(price, room_type, 0, "", "", location));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomPrices");
		}

		return roomPrices;		
	}

	public List<OwnerPriceBean> getRoomPrices(String location, int today_day, int today_month, int today_year) {
		List<OwnerPriceBean> roomPrices = new ArrayList<OwnerPriceBean>();

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, rt.price, d.start_date, d.end_date, " +
					"d.discounted_price, h.location from room_type rt " +
					"join discount d on (d.room_type_id = rt.id) " +
					"join hotel h on (h.id = d.hotel_id) " +
					"where h.location = '" + location + "' " +
					"and '" + today_year + "-" + today_month + "-" + today_day + "' " +
					"between d.start_date and d.end_date " +
					"group by rt.room_type, rt.price, d.start_date, d.end_date, d.discounted_price, h.location";
			ResultSet res = stmnt.executeQuery(query_cast);

			while (res.next()) {
				String room_type = res.getString("room_type");
				int price = res.getInt("price");
				String start_date = res.getString("start_date");
				String end_date = res.getString("end_date");
				int discounted_price = res.getInt("discounted_price");
				String loc = res.getString("location");

				roomPrices.add(new OwnerPriceBean(price, room_type, discounted_price, start_date, end_date, loc));
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getRoomPrices");
		}

		return roomPrices;		
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

	public List<RoomDTO> getRoomScheduleByCustomerBookingID(int custBookingID) {
		ResultSet result = null;
		List <RoomDTO> rooms = new ArrayList<RoomDTO>();
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT "
							+ "room_id "
							+ "FROM "
							+ "ROOM_SCHEDULE "
							+ "WHERE "
							+ "customer_booking_id=?");
			ps.setInt(1, custBookingID);
			result = ps.executeQuery();
			while (result.next()) {
				rooms.add(getRoomByID(result.getInt("room_id")));
			}
		} catch (Exception e) {

		}
		return rooms;
	}

	public BookingDTO getCustomerBookingFromCode(String code) {
		BookingDTO booking = null;
		try {
			PreparedStatement ps = connection.prepareStatement("select * from booking_unique where code=?");
			ps.setString(1, code);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				booking = getCustomerBookingByID(result.getInt("customer_booking_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return booking;
	}

	public String createBookingCode(int custBookingID) {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		String code;
		int length = 30;
		for (int i = 0; i < length; i++) {
			sb.append(chars[rand.nextInt(chars.length)]);
		}
		code = sb.toString();
		if (getCustomerBookingFromCode(code) != null) {
			code = createBookingCode(custBookingID);
		}



		return code;
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

		return new BookingDTO(cb_id, new CustomerDTO(c_id, c_Firstname, c_LastName), startDate, endDate, getRoomScheduleByCustomerBookingID(cb_id));
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
