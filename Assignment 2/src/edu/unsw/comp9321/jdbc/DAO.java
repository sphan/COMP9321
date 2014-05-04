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
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.logic.RoomTypeSearch;

public class DAO {
	//This class should be the only class to talk to database
	//Just keep adding more functions as we need them
	//TODO must add insert functions here as well

	static Logger logger = Logger.getLogger(DAO.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

	public DAO () throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	
	public List<RoomTypeSearch> getHotelRoomTypes(String location) {
		List<RoomTypeSearch> roomTypeList = new ArrayList<RoomTypeSearch>();
		
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
					+ "room_type rt "
					+ "on "
					+ "(r.room_type_id=rt.id) "
					+ "join "
					+ "hotel h "
					+ "on "
					+ "(h.id=r.hotel_id) "
					+ "where "
					+ "h.location='"+location+"' "
					+ "group by rt.room_type, rt.price";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while (res.next()) {
				String room_type = res.getString("room_type");
				int price = res.getInt("price");
				int count = res.getInt("count");
				roomTypeList.add(new RoomTypeSearch(room_type, price, count));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roomTypeList;
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
					+ "c.name,"
					+ "c.username,"
					+ "c.password "
					+ "FROM CUSTOMER_BOOKING cb "
					+ "JOIN CUSTOMER c "
					+ "ON (cb.CUSTOMER_ID=c.ID)";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while (res.next()) {
				bookings.add(rebuildBooking(res));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookings;
	}
	
	public BookingDTO addBooking(int custID, int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
		BookingDTO returnRes = null;
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
			String query_cast = "SELECT "
					+ "cb.id as cbid,"
					+ "cb.start_date,"
					+ "cb.end_date,"
					+ "c.id as cid,"
					+ "c.name,"
					+ "c.username,"
					+ "c.password "
					+ " FROM CUSTOMER_BOOKING CB "
					+ "JOIN CUSTOMER C "
					+ "ON (CB.CUSTOMER_ID=C.ID) "
					+ "WHERE CB.ID="+last_inserted_id;
			res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			while (res.next()) {
				returnRes = rebuildBooking(res);			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnRes;
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
	
	public CustomerDTO addCustomer(String name, String userName, String password) {
		name = name.toUpperCase();
		userName = userName.toUpperCase();
		CustomerDTO returnResult = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CUSTOMER VALUES(DEFAULT,?,?,?)");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, userName);
			preparedStatement.setString(3, password);
			preparedStatement.executeUpdate();
			//added to db
			//now get from db and return;
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * FROM CUSTOMER WHERE USERNAME='"+userName+"'";
			ResultSet res = stmnt.executeQuery(query_cast);
			while (res.next()) {
				int sqlid = res.getInt("id");
				String sqlName = res.getString("name");
				String sqlUserName = res.getString("username");
				String sqlPpassword = res.getString("password");
				returnResult = new CustomerDTO(sqlid, sqlName, sqlUserName, sqlPpassword);
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
				String name = res.getString("name");
				String userName = res.getString("username");
				String password = res.getString("password");
				customers.add(new CustomerDTO(id, name, userName, password));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customers;
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
			String query_cast = "SELECT id, name, username, password FROM STAFF WHERE username = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(query_cast);
			count_stmnt.setString(1, username);
			ResultSet res = count_stmnt.executeQuery();
			res.next();
			int numRows = res.getInt(1);
			logger.info("The result set size is " + numRows);
			
			int id = res.getInt("id");
			String name = res.getString("name");
			String usr = res.getString("username");
			String pw = res.getString("password");
			staff = new StaffDTO(id, name, usr, pw);
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
		String c_name = res.getString("name");
		String c_userName = res.getString("username");
		String c_password = res.getString("password");
		
		int cb_id = res.getInt("cbid");
		Calendar startDate = new GregorianCalendar();
		startDate.setTime(res.getDate("start_date"));
		Calendar endDate = new GregorianCalendar();
		startDate.setTime(res.getDate("end_date"));
		return new BookingDTO(cb_id, new CustomerDTO(c_id, c_name, c_userName, c_password), startDate, endDate);
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
