package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.BookingSelection;
import edu.unsw.comp9321.bean.SearchDetailsBean;
import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

/**
 * Servlet implementation class Booking
 */
@WebServlet(urlPatterns="/booking",displayName="BookingServlet")
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PassByRef pbr = new PassByRef();
		String nextPage = "";
		String[] roomTypeName = request.getParameterValues("roomTypeName[]");
		String[] roomTypePrice = request.getParameterValues("roomTypePrice[]");
		String[] roomTypeCount = request.getParameterValues("roomTypeCount[]");
		assert(roomTypeName.length==roomTypePrice.length && roomTypePrice.length==roomTypeCount.length);
		//###############################################################
		if (request.getParameter("action").equals("submit")) {

			boolean bookingEmpty = false;
			BookingListBean blb = (BookingListBean) request.getSession().getAttribute("booking");
			blb.clearBookingList();//to prepare for a new search
			int index = 1;
			for (int i = 0; i < roomTypeName.length; i++) {
				if (Integer.parseInt(roomTypeCount[i]) != 0) {
					for (int j = 0; j < Integer.parseInt(roomTypeCount[i]); j++) {
						blb.addBookingSelection(new BookingSelection(index++, roomTypeName[i], roomTypePrice[i]));
					}
				}
			}
			if (blb.getList().size() == 0) {
				bookingEmpty = true;
			}

			int totalPrice = 0;
			for (int i = 0; i < roomTypeName.length; i++) {
				totalPrice += Integer.parseInt(roomTypeCount[i]) * Integer.parseInt(roomTypePrice[i]);
			}
			request.setAttribute("totalPrice", totalPrice);
			request.setAttribute("bookingEmpty", bookingEmpty);
			nextPage = "booking.jsp";
		}
		//###############################################################
		else if (request.getParameter("action").equals("calculate total")) {
			DAO dao = new DAO(pbr);
			SearchDetailsBean sdb = (SearchDetailsBean) request.getSession().getAttribute("searchDetails");

			int totalPrice = 0;
			for (int i = 0; i < roomTypeName.length; i++) {
				totalPrice += Integer.parseInt(roomTypeCount[i]) * Integer.parseInt(roomTypePrice[i]);
			}
			request.setAttribute("totalPrice", totalPrice);
			request.setAttribute("location", sdb.getLocation());
			request.setAttribute("maxPrice", sdb.getMaxPrice());
			request.setAttribute("roomTypeList", dao.getHotelRoomSelection(sdb.getLocation(), sdb.getMaxPrice(), sdb.getStartDay(), sdb.getStartMonth(), sdb.getStartYear(), sdb.getEndDay(), sdb.getEndMonth(), sdb.getEndYear()));

			nextPage = "searchResults.jsp";
		} else {
			nextPage = "customerMain.jsp";
			pbr.addErrorMessage("an error occurred, please try it again");
		}
		//###############################################################

		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}
}
