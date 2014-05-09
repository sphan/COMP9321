package edu.unsw.comp9321.logic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.jdbc.DAO;

@WebServlet(urlPatterns="/owner",displayName="OwnerServlet")
public class OwnerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public OwnerServlet() {
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
		String action = request.getParameter("action");
		String nextPage = "ownerPage.jsp";
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		
		if (action.equalsIgnoreCase("search")) {
			nextPage = Command.ownerSearch(request, dao);
		} else if (action.equalsIgnoreCase("set discount price")) {
			nextPage = "setDiscountPrice.jsp";
			Command.displayAllRoomPrices(request, dao);
		} else if (action.equalsIgnoreCase("priceSearch")) {
			nextPage = Command.staffSearchRoomPrice(request, dao);
		} else if (action.equalsIgnoreCase("back to main")) {
			Command.displayAllOccupancies(request, dao);
		}
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}
}
