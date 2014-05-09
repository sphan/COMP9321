package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.BookingSelection;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.CustomerDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.RoomDTO;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

/**
 * Servlet implementation class Payment
 */
@WebServlet(urlPatterns="/payment",displayName="PaymentServlet")
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentServlet() {
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
		// TODO booking and payment has been confirmed, we must create bookings and make email etc
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		String nextPage = "";
		
		String firstName = request.getParameter("fname");
		String lastName = request.getParameter("lname");
		String email = request.getParameter("email");
		String creditCardNumber = request.getParameter("creditcard");
		String expirationMonth = request.getParameter("expireMM");
		String expirationYear = request.getParameter("expireYY");
		if (firstName.equals("")||lastName.equals("")||email.equals("")||creditCardNumber.equals("")||expirationMonth.equals("")||expirationYear.equals("")||
				firstName==null||lastName==null||email==null||creditCardNumber==null||expirationMonth==null||expirationYear==null) {
			nextPage = "booking.jsp";
			pbr.addErrorMessage("one of the fields are invalid or incomplete");
		} else {
			BookingListBean blb = (BookingListBean) request.getSession().getAttribute("booking");
			CustomerDTO cust = dao.addCustomer(firstName, lastName);
			BookingDTO booking = dao.addCustomerBooking(
					cust.getId(), blb
					);
			request.setAttribute("bookingDetails", booking);
			
			nextPage = "bookingInfo.jsp";
		}
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

}
