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

import edu.unsw.comp9321.exception.ServiceLocatorException;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.DBConnectionFactory;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet(urlPatterns="/control",displayName="ControlServlet")
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControlServlet() {
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
		try {
			int startDay = Integer.parseInt(request.getParameter("startday"));
			int startMonth = Integer.parseInt(request.getParameter("startmonth"));
			int startYear = Integer.parseInt(request.getParameter("startyear"));
			int endDay = Integer.parseInt(request.getParameter("endday"));
			int endMonth = Integer.parseInt(request.getParameter("endmonth"));
			int endYear = Integer.parseInt(request.getParameter("endyear"));
			
			String city = request.getParameter("city");
			int maxPrice = Integer.MAX_VALUE;	//will display all rooms if nothing is set
			try {
				maxPrice = Integer.parseInt(request.getParameter("maxPrice"));
			} catch (NumberFormatException nfe) {/*catch exception and do nothing*/}
			
			List<RoomTypeDTO> roomTypeList = dao.getHotelRoomTypes(city, maxPrice);
			request.setAttribute("location", city);
			request.setAttribute("maxPrice", maxPrice);
			request.setAttribute("roomTypeList", roomTypeList);
			
			
		} catch (NumberFormatException nfe) {
			pbr.addErrorMessage("One of the values are invalid");
		}
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + "searchResults.jsp");
		rd.forward(request, response);


	}
}
