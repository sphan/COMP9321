package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321.exception.ServiceLocatorException;

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

	//return all pending bookings
	public List<BookingDTO> getAllPendingBookings() {
		return null;
	}

	//return all bookings including past completed ones
	//unsure if we actually need this
	public List<BookingDTO> getAllBookings() {
		return null;
	}

	//return a single booking from ID, plan to use for
	//displaying individual booking information
	public BookingDTO getBooking(String ID) {
		return null;
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



}
