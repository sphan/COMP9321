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

import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

/**
 * Servlet implementation class Booking
 */
@WebServlet("/Booking")
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
		String nextPage = "";
		boolean searchEmpty = false;

		String location = request.getParameter("location");
		int maxPrice = Integer.parseInt(request.getParameter("maxPrice"));
		List<RoomTypeDTO> bookingChoices = dao.getHotelRoomTypes(location, maxPrice);
		
		if (bookingChoices.size() > 0) {
			//if something in list, return those things in list
			for (RoomTypeDTO rts : bookingChoices) {
				int count = Integer.parseInt(request.getParameter(rts.getRoomType()));
				rts.setCount(count);
			}
			
			//booking choices contain choices with count 0, remove them
			for (int i = 0; i < bookingChoices.size(); i++) {
				if (bookingChoices.get(i).getCount() == 0) {
					bookingChoices.remove(i);
					i--;//since we removed this index from the list array elements shift down
				}
			}
			
			request.setAttribute("bookingChoices", bookingChoices);
			nextPage = "booking.jsp";
		}
		//after filtering out rooms of count 0, check if there are any rooms left
		if (bookingChoices.size() == 0) {
			//if nothing in list, return message telling them to retry with different values
			searchEmpty = true;
			nextPage = "booking.jsp";//send them to another page, but bookings for now.
		}
		//TODO merge payment content in bookings jsp, so we dont have to make another page
		request.setAttribute("searchEmpty", searchEmpty);
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

}
