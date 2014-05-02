package edu.unsw.comp9321.jdbc;

import java.sql.Connection;
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
		return null;
	}
	
	//return all unused rooms, plan to use this for allocating
	//rooms to bookings
	public List<RoomDTO> getAllAvailableRooms() {
		return null;
	}
	
	//return all customers, unsure if we need lists of customers
	public List<CustomerDTO> getAllCustomers() {
		return null;
	}
	
	//return a single customer based on param
	public CustomerDTO getCustomer(String param) {
		return null;
	}
	
	//return all staff in hotl
	public List<StaffDTO> getAllStaff() {
		return null;
	}
	
	

}
