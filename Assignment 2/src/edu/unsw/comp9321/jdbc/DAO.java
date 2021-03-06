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
import edu.unsw.comp9321.bean.BookingRoomDetailBean;
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
	
	public PassByRef getPbr() {
		return pbr;
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
		ResultSet discountPrice = null;

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
							+ "rt.room_type, "
							+ "rt.price, "
							+ "count(rt.room_type) as count "
							+ "from room_schedule rs "
							+ "join "
							+ "room_type rt "
							+ "on "
							+ "(rs.room_type_id=rt.id) "
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
							+ "and "
							+ "((cb.start_date between ? and ?) "
							+ "or "
							+ "(cb.end_date between ? and ?)) "
							+ "group by "
							+ "rt.room_type, "
							+ "rt.price");
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
				
				ps = connection.prepareStatement("" +
						"select d.discounted_price from discount d " +
						"join room_type rt on (rt.id = d.room_type_id) " +
						"join hotel h on (h.id = d.hotel_id) " +
						"where h.location = ? " +
						"and rt.room_type = ? " +
						"and ((? between d.start_date and d.end_date )" +
						"or (? between d.start_date and d.end_date))");
				ps.setString(1, sdb.getLocation());
				ps.setString(2, roomType);
				ps.setString(3, sdb.getStartYear()+"-"+sdb.getStartMonth()+"-"+sdb.getStartDay());
				ps.setString(4, sdb.getEndYear()+"-"+sdb.getEndMonth()+"-"+sdb.getEndDay());
				discountPrice = ps.executeQuery();
				
				if (discountPrice.next()) {
					RoomTypeDTO roomTypeDTO = new RoomTypeDTO(roomType, discountPrice.getInt("discounted_price"), count);
					roomTypeDTO.setDiscounted(true);
					roomTypeList.add(roomTypeDTO);
				} else {
					roomTypeList.add(new RoomTypeDTO(roomType, price, count));
				}
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roomTypeList;
	}
	
	/**
	 * Get list of available rooms by the given room type and hotel location.
	 * @param roomType The room type for which rooms should be of.
	 * @param location The locations where the rooms should belong.
	 * @return A list of room details for the booking.
	 */
	public List<BookingRoomDetailBean> getAvailableRooms(int bookingID) {
		List<BookingRoomDetailBean> availableRooms = new ArrayList<BookingRoomDetailBean>();
		List<BookingRoomDetailBean> requestDetail = getBookingRoomDetails(bookingID);
		ResultSet results = null;
		
		try {
			for (BookingRoomDetailBean rd : requestDetail) {
				PreparedStatement ps = connection.prepareStatement("" +
						"select r.id as room_id, h.id as hotel_id from room r " +
						"join room_type rt on (rt.id = r.room_type_id) " +
						"join hotel h on (h.id = r.hotel_id) " +
						"where r.availability = 'available' " +
						"and rt.room_type = ? and h.location = ?");
				ps.setString(1, rd.getRoomType());
				ps.setString(2, rd.getHotel().getLocation());
				results = ps.executeQuery();
				
				while (results.next()) {
					int room_id = results.getInt("room_id");
					int hotel_id = results.getInt("hotel_id");
					availableRooms.add(new BookingRoomDetailBean(rd.getRoomType(), getHotelByID(hotel_id), getRoomByID(room_id)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return availableRooms;
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
							+ "c.last_name, "
							+ "h.id as hid "
							+ "FROM CUSTOMER_BOOKING cb "
							+ "JOIN CUSTOMER c "
							+ "ON (cb.CUSTOMER_ID=c.ID) "
							+ "JOIN "
							+ "HOTEL h "
							+ "ON (h.id=cb.hotel_id)";
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
					+ "c.last_name, "
					+ "h.id as hid "
					+ "FROM CUSTOMER_BOOKING cb "
					+ "JOIN CUSTOMER c "
					+ "ON (cb.CUSTOMER_ID=c.ID) JOIN HOTEL h on (h.id=cb.hotel_id) "
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
					+ "cb.id as cbid, "
					+ "cb.start_date, "
					+ "cb.end_date, "
					+ "c.id as cid, "
					+ "c.first_name, "
					+ "c.last_name, "
					+ "h.id as hid "
					+ "FROM CUSTOMER_BOOKING cb "
					+ "JOIN CUSTOMER c "
					+ "ON (cb.CUSTOMER_ID=c.ID) "
					+ "JOIN HOTEL h "
					+ "ON (h.id=cb.hotel_id) "
					+ "WHERE c.first_name = '" + customerName.toUpperCase() + "'";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+ res.getFetchSize());
			while (res.next()) {
				bookings.add(rebuildBooking(res));
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
	
	public void updateRoomSchedule(int custBookingID, int roomID, String room_type) {
		PreparedStatement ps = null;
		ResultSet results = null;
		
		try {
			
			ps = connection.prepareStatement("" +
					"select rs.id from room_schedule rs " +
					"join room_type rt on (rt.id = rs.room_type_id) " +
					"where rt.room_type = ? and room_id is NULL and rs.customer_booking_id = ?");
			ps.setString(1, room_type);
			ps.setInt(2, custBookingID);
			results = ps.executeQuery();
			
			
			if (results.next()) {
				ps = connection.prepareStatement("update room_schedule set room_id = ? " +
						"where id = ?");
				ps.setInt(1, roomID);
				ps.setInt(2, results.getInt("id"));
				ps.executeUpdate();
			}
		} catch (Exception e) {
			
		}
	}

	public void addRoomSchedule(int custBookingID, String roomType, String location, String startDate, String endDate, boolean extraBed) {
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;

		try {
			ps = connection.prepareStatement("INSERT INTO ROOM_SCHEDULE VALUES(DEFAULT, null,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, custBookingID);
			ps.setInt(2, getRoomTypeIDByName(roomType));
			ps.setInt(3, (extraBed) ? 1 : 0);
			ps.executeUpdate();

			generatedKeys = ps.getGeneratedKeys();
			if (!generatedKeys.next()) {
				throw new EmptyResultException();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
							blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay(), bs.isExtraBed()
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
		if (room_id == -1) {
			return room;
		}

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

	/**
	 * Return the default price of the given room type located
	 * at the given hotel location.
	 * @param location The hotel location. E.g. Sydney, Melbourne.
	 * @param room_type The room type, e.g. single, double.
	 * @return The current price of the given room type at the given
	 * 		location.
	 */
	public OwnerPriceBean getDefaultRoomPrice(String location, String room_type) {
		OwnerPriceBean roomPrice = null;
		
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, rt.price, h.location from room_type rt " +
					"join room r on (r.room_type_id = rt.id) " +
					"join hotel h on (h.id = r.hotel_id) " +
					"where h.location = '" + location + "' " +
					"and rt.room_type = '" + room_type + "' " +
					"group by rt.room_type, rt.price, h.location";
			ResultSet res = stmnt.executeQuery(query_cast);
			
			if (res.next()) {
				String type = res.getString("room_type");
				int price = res.getInt("price");
				String loc = res.getString("location");
				roomPrice = new OwnerPriceBean(price, type, 0, null, null, loc);
			}
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in getCurrRoomPrice");
		}
		return roomPrice;
	}

	public List<OwnerPriceBean> getRoomPrices(String location, int today_day, int today_month, int today_year) {
		List<OwnerPriceBean> roomPrices = new ArrayList<OwnerPriceBean>();
		String loc_query = "";

		if (!location.isEmpty()) {
			loc_query = "h.location = '" + location + "' and ";
		}

		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, rt.price, d.start_date, d.end_date, " +
					"d.discounted_price, h.location from room_type rt " +
					"join discount d on (d.room_type_id = rt.id) " +
					"join hotel h on (h.id = d.hotel_id) " +
					"where " + loc_query +
					"'" + today_year + "-" + today_month + "-" + today_day + "' " +
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

	public List<OwnerPriceBean> getRoomPrices(String location) {
		List<OwnerPriceBean> roomPrices = new ArrayList<OwnerPriceBean>();
		int today_day = Command.getCurrentDay();
		int today_month = Command.getCurrentMonth();
		int today_year = Command.getCurrentYear();

		String loc_query = "";

		if (!location.isEmpty()) {
			loc_query = "h.location = '" + location + "' and ";
		}
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.room_type, rt.price from room_type rt " +
					"join room r on (r.room_type_id = rt.id) " +
					"join hotel h on (h.id = r.hotel_id) " +
					"where " + loc_query +
					"rt.id not in " +
					"(select rt2.id from room_type rt2 " +
					"join discount d on (d.room_type_id = rt2.id) " +
					"join hotel h on (h.id = d.hotel_id) " +
					"where " + loc_query +
					"'" + today_year + "-" + today_month + "-" + today_day + "' " +
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
	
	public void addDiscount(OwnerPriceBean newPrice) {
		try {
			Statement stmnt = connection.createStatement();
			String query_cast = "select rt.id as rt_id, h.id as h_id from room_type rt " +
					"join room r on (r.room_type_id = rt.id) " +
					"join hotel h on (h.id = r.hotel_id) " +
					"where rt.room_type = '" + newPrice.getRoomType() + "' " +
					"and h.location = '" + newPrice.getLocation() + "' " +
					"group by rt.id, h.id";
			ResultSet res = stmnt.executeQuery(query_cast);
			int room_type_id = 0;
			int hotel_id = 0;
			if (res.next()) {
				room_type_id = res.getInt("rt_id");
				hotel_id = res.getInt("h_id");
			}
			
			PreparedStatement prepStatement = connection.prepareStatement("" +
					"INSERT INTO DISCOUNT " +
					"VALUES(DEFAULT, ?, ?, ?, ?, ?)");
			prepStatement.setInt(1, room_type_id);
			prepStatement.setString(2, newPrice.getDiscountStartDate());
			prepStatement.setString(3, newPrice.getDiscountEndDate());
			prepStatement.setInt(4, newPrice.getDiscountPrice());
			prepStatement.setInt(5, hotel_id);
			prepStatement.executeUpdate();
			
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
			pbr.addErrorMessage("SQLException in addDiscount");
		}
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
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			
			if (res.next()) {
				int id = res.getInt("id");
				String first_name = res.getString("first_name");
				String last_name = res.getString("last_name");
				customer = new CustomerDTO(id, first_name, last_name);
			}
			
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
	
	public RoomScheduleDTO getRoomScheduleByID(int roomScheduleID) {
		RoomScheduleDTO roomSchedule = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		try {
			ps = connection.prepareStatement(
					"select "
					+ "rs.id, "
					+ "rs.room_id, "
					+ "rs.customer_booking_id, "
					+ "rt.room_type, "
					+ "rs.extra_bed "
					+ "from "
					+ "room_schedule rs "
					+ "join room_type rt "
					+ "on "
					+ "(rs.room_type_id=rt.id) "
					+ "where "
					+ "rs.id=?");
			ps.setInt(1, roomScheduleID);
			result = ps.executeQuery();
			
			if (result.next()) {
				int id = result.getInt("id");
				int roomID = result.getObject("room_id") != null ? result.getInt("room_id") : -1;
				int customerBookingID = result.getInt("customer_booking_id");
				String roomType = result.getString("room_type");
				int extraBed = result.getInt("extra_bed");
				roomSchedule = new RoomScheduleDTO(id, getRoomByID(roomID), roomType, ((extraBed == 0) ? false : true), customerBookingID);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roomSchedule;
	}

	public List<RoomScheduleDTO> getRoomScheduleByCustomerBookingID(int custBookingID) {
		ResultSet result = null;
		List <RoomScheduleDTO> roomSchedules = new ArrayList<RoomScheduleDTO>();
		try {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT "
							+ "id "
							+ "FROM "
							+ "ROOM_SCHEDULE "
							+ "WHERE "
							+ "customer_booking_id=?");
			ps.setInt(1, custBookingID);
			result = ps.executeQuery();
			while (result.next()) {
				roomSchedules.add(getRoomScheduleByID(result.getInt("id")));
			}
		} catch (Exception e) {

		}
		return roomSchedules;
	}
	
	public List<BookingRoomDetailBean> getBookingRoomDetails(int bookingID) {
		ResultSet result = null;
		List<BookingRoomDetailBean> roomDetails = new ArrayList<BookingRoomDetailBean>();
		
		try {
			PreparedStatement ps = connection.prepareStatement(
					"select rs.room_id, rt.room_type, h.id, h.name, h.location from room_type rt " + 
					"join room_schedule rs on (rs.room_type_id = rt.id) " + 
					"join customer_booking cb on (cb.id = rs.customer_booking_id) " +
					"join hotel h on (h.id = cb.hotel_id) " +
					"where rs.customer_booking_id = ?");
			ps.setInt(1, bookingID);
			result = ps.executeQuery();
			
			while (result.next()) {
				int room_id = result.getInt("room_id");
				String room_type = result.getString("room_type");
				int hotel_id = result.getInt("id");
				
				roomDetails.add(new BookingRoomDetailBean(room_type, getHotelByID(hotel_id), getRoomByID(room_id)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return roomDetails;
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
		
		try {
			ResultSet generatedKeys = null;
			PreparedStatement ps = connection.prepareStatement("insert into booking_unique values(default,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, code);
			ps.setInt(2, custBookingID);
			ps.executeUpdate();
			
			generatedKeys = ps.getGeneratedKeys();
			
			if (generatedKeys.next()) {
				
			} else {
				throw new Exception("Key was not generated");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return code;
	}
	

	public HotelDTO getHotelByID(int hotel_id) {
		HotelDTO hotel = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			ps = connection.prepareStatement("SELECT * FROM HOTEL WHERE id=?");
			ps.setInt(1, hotel_id);
			result = ps.executeQuery();
			if (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String location = result.getString("location");
				hotel = new HotelDTO(id, name, location);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hotel;

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

		int h_id = res.getInt("hid");

		return new BookingDTO(cb_id, new CustomerDTO(c_id, c_Firstname, c_LastName), startDate, endDate, getRoomScheduleByCustomerBookingID(cb_id), getHotelByID(h_id));
	}



}
