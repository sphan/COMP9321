package edu.unsw.comp9321.logic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.DAO;

/**
 * Servlet implementation class URL
 */
@WebServlet("/URL")
public class URLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public URLServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getRequestURI().replaceAll(".*\\/", ""); // get the code from url
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		String nextPage;
		
		BookingDTO booking=dao.getCustomerBookingFromCode(code);
		
		if (booking != null) {
			request.setAttribute("bookingDetails", booking);
			System.out.println(booking.getRooms().size());
			nextPage = "bookingInfo.jsp";
		} else {
			pbr.addErrorMessage("The URL entered is invalid");
			System.out.println("not book");
			nextPage = "customerMain.jsp";
		}
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
