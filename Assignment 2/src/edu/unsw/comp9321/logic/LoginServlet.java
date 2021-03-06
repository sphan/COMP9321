package edu.unsw.comp9321.logic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.jdbc.DAO;

@WebServlet(urlPatterns="/login",displayName="LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
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
		String nextPage = "customerMain.jsp";
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		
		if (action.equalsIgnoreCase("Login")) {
			nextPage = Command.staffLogin(request, dao);
			if (nextPage.equals("customerMain.jsp"))
				pbr.addErrorMessage("User not found.");
		} else if (action.equalsIgnoreCase("logout")) {
			
		}
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}
}