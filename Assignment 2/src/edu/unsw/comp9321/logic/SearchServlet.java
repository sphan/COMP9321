package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.SearchDetailsBean;
import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.DBConnectionFactory;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet(urlPatterns="/search",displayName="SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
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
		SearchDetailsBean sdb = (SearchDetailsBean) request.getSession().getAttribute("searchDetails");
		if (sdb == null) {
			//session expired, create new sdb
			sdb = new SearchDetailsBean();
		}
		//######################################
		sdb.setStartDay(Integer.parseInt(request.getParameter("startday")));
		sdb.setStartMonth(Integer.parseInt(request.getParameter("startmonth")));
		sdb.setStartYear(Integer.parseInt(request.getParameter("startyear")));
		sdb.setEndDay(Integer.parseInt(request.getParameter("endday")));
		sdb.setEndMonth(Integer.parseInt(request.getParameter("endmonth")));
		sdb.setEndYear(Integer.parseInt(request.getParameter("endyear")));
		sdb.setLocation(request.getParameter("location"));
		System.out.println(sdb.getLocation());
		if (sdb.getLocation().equals("Select") || sdb.getLocation() == null) {
			pbr.addErrorMessage("Please select a city");
		}
		else if (!Command.isPresentFutureDate(sdb.getStartYear(), sdb.getStartMonth(), sdb.getStartDay()) || !Command.isPresentFutureDate(sdb.getEndYear(),  sdb.getEndMonth(),  sdb.getEndDay())) {
			pbr.addErrorMessage("You cannot book in the past");
		}
		else if (!Command.isValidDateRange(sdb.getStartYear(), sdb.getStartMonth(), sdb.getStartDay(), sdb.getEndYear(), sdb.getEndMonth(), sdb.getEndDay())) {
			pbr.addErrorMessage("Date range is invalid");
		}
		else {
			try {
				sdb.setMaxPrice(Integer.parseInt(request.getParameter("maxPrice")));
			} catch (NumberFormatException nfe) {/*catch exception and do nothing*/}
			//######################################

			request.setAttribute("roomTypeList", dao.getHotelRoomSelection(sdb));
		}
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + "searchResults.jsp");
		rd.forward(request, response);
	}
}
