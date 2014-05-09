package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.jdbc.DAO;

@WebServlet(urlPatterns="/staff",displayName="StaffServlet")
public class StaffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StaffServlet() {
		super();
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
		String action = request.getParameter("action");
		String nextPage = "staffPage.jsp";
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		
		if (action.equalsIgnoreCase("Search")) {
			nextPage = Command.staffSearch(request, dao);
			System.out.println("nextPage: " + nextPage);
		} else if (action.equalsIgnoreCase("select booking")) {
			nextPage = Command.staffSelectBooking(request, dao);
			if (nextPage.equalsIgnoreCase("staffPage.jsp")) {
				pbr.addErrorMessage("Please select a booking.");
				Command.displayAllBookings(request, dao);
			}
		} else if (action.equalsIgnoreCase("back")) {
			Command.displayAllBookings(request, dao);
		} else if (action.equalsIgnoreCase("checkIn")) {
			nextPage = Command.checkIn(request, dao, pbr);
		}
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}
}
