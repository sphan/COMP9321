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
import edu.unsw.comp9321.jdbc.BookingDTO;
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
		DAO dao = new DAO(pbr);
		String nextPage;
		String[] roomTypeName = request.getParameterValues("roomTypeName[]");
		String[] roomTypePrice = request.getParameterValues("roomTypePrice[]");
		String[] roomTypeCount = request.getParameterValues("roomTypeCount[]");
		assert(roomTypeName.length==roomTypePrice.length && roomTypePrice.length==roomTypeCount.length);
		//###############################################################
		String code = request.getParameter("URLhidden");
		request.setAttribute("URLhidden", code);
		pbr.addErrorMessage(code);
		
		SearchDetailsBean sdb = (SearchDetailsBean) request.getSession().getAttribute("searchDetails");
		if (sdb == null) {
			pbr.addErrorMessage("session expired, please try again");
			nextPage = "customerMain.jsp";
		}
		else if (request.getParameter("action").equals("submit")) {
			BookingListBean blb = (BookingListBean) request.getSession().getAttribute("booking");
			
			if (blb == null) {
				pbr.addErrorMessage("session expired, please try again");
				nextPage = "customerMain.jsp";
			} else {
				blb.clearBookingList();//to prepare for a new search
				blb.setStartDay(sdb.getStartDay());
				blb.setStartMonth(sdb.getStartMonth());
				blb.setStartYear(sdb.getStartYear());
				blb.setEndDay(sdb.getEndDay());
				blb.setEndMonth(sdb.getEndMonth());
				blb.setEndYear(sdb.getEndYear());
				blb.setLocation(sdb.getLocation());
				int index = 1;
				for (int i = 0; i < roomTypeName.length; i++) {
					if (Integer.parseInt(roomTypeCount[i]) != 0) {
						for (int j = 0; j < Integer.parseInt(roomTypeCount[i]); j++) {
							blb.addBookingSelection(new BookingSelection(index++, roomTypeName[i], roomTypePrice[i], false));
						}
					}
				}
				BookingDTO booking = dao.getCustomerBookingFromCode(code);
				String fname = booking.getCustomer().getFirstName();
				String lname = booking.getCustomer().getLastName();
				request.setAttribute("firstName", fname);
				request.setAttribute("lastName", lname);
				nextPage = "booking.jsp";
			}
		}
		//###############################################################
		else if (request.getParameter("action").equals("calculate total")) {
			

			int totalPrice = 0;
			for (int i = 0; i < roomTypeName.length; i++) {
				totalPrice += Integer.parseInt(roomTypeCount[i]) * Integer.parseInt(roomTypePrice[i]);
			}
			request.setAttribute("totalPrice", totalPrice);
			List<RoomTypeDTO> roomTypeList = dao.getHotelRoomSelection(sdb);
			
			if (roomTypeCount != null) {
				for (int i = 0; i < roomTypeCount.length; i++) {
					if (Integer.parseInt(roomTypeCount[i]) != 0) {
						roomTypeList.get(i).setSelectValue(Integer.parseInt(roomTypeCount[i]));
					}
				}
			}
			request.setAttribute("roomTypeList", roomTypeList);

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
