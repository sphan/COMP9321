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
		//#####################
		BookingListBean blb = (BookingListBean) request.getSession().getAttribute("booking");
		String[] roomTypeName = request.getParameterValues("roomTypeName[]");
		String[] roomTypePrice = request.getParameterValues("roomTypePrice[]");
		String[] roomTypeCount = request.getParameterValues("roomTypeCount[]");
		assert(roomTypeName.length==roomTypePrice.length && roomTypePrice.length==roomTypeCount.length);
		for (int i = 0; i < roomTypeName.length; i++) {
			if (Integer.parseInt(roomTypeCount[i]) != 0) {
				blb.addBookingSelection(new BookingSelection(roomTypeName[i], roomTypePrice[i], roomTypeCount[i]));
			}
		}
		//#####################
		request.setAttribute("searchEmpty", false);
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + "booking.jsp");
		rd.forward(request, response);
	}
}
