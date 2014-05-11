package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.bean.BookingListBean;
import edu.unsw.comp9321.bean.BookingSelection;
import edu.unsw.comp9321.bean.URLBookingBean;
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

		String codehidden = request.getParameter("URLhidden");
		request.setAttribute("URLhidden", codehidden);

		BookingListBean blb = (BookingListBean) request.getSession().getAttribute("booking");
		if (blb == null) {
			pbr.addErrorMessage("Session Expired Try again");
		} else {
			String[] extraBed = request.getParameterValues("extrabed");

			for (BookingSelection bs : blb.getList()) {
				bs.setExtraBed(false);	//set all to false as the correct ones will be set to true to ensure no trues are carried over
			}
			if (extraBed != null) {//if all checkboxes are unticked, then extraBed is null
				for (BookingSelection bs : blb.getList()) {
					for(String s : extraBed) {
						if (bs.getIndex() == Integer.parseInt(s)) {
							bs.setExtraBed(true);
						}
					}
				}
			}
			if (request.getParameter("action").equals("update total")) {
				if (codehidden != null && !codehidden.equals("")) {
					BookingDTO booking = dao.getCustomerBookingFromCode(codehidden);
					String fname = booking.getCustomer().getFirstName();
					String lname = booking.getCustomer().getLastName();
					request.setAttribute("firstName", fname);
					request.setAttribute("lastName", lname);
				}
				nextPage = "booking.jsp";
			}
			else if (request.getParameter("action").equals("confirm")) {
				String firstName = request.getParameter("fname");
				String lastName = request.getParameter("lname");
				String email = request.getParameter("email");
				String creditCardNumber = request.getParameter("creditcard");
				String expirationMonth = request.getParameter("expireMM");
				String expirationYear = request.getParameter("expireYY");

				if (!Command.validateEmail(email) || !Command.validateCreditCard(creditCardNumber)||
						firstName==null||lastName==null||email==null||creditCardNumber==null||expirationMonth==null||
						expirationYear==null||firstName.equals("")||lastName.equals("")||email.equals("")||creditCardNumber.equals("")||expirationMonth.equals("")||expirationYear.equals("")) {
					if (codehidden != null && !codehidden.equals("")) {
						BookingDTO booking = dao.getCustomerBookingFromCode(codehidden);
						String fname = booking.getCustomer().getFirstName();
						String lname = booking.getCustomer().getLastName();
						request.setAttribute("firstName", fname);
						request.setAttribute("lastName", lname);
					}
					nextPage = "booking.jsp";
					pbr.addErrorMessage("one of the fields are invalid or incomplete");
				} else {

					if (codehidden == null || codehidden.equals("")) {
						CustomerDTO cust = dao.addCustomer(firstName, lastName);
						BookingDTO booking = dao.addCustomerBooking(
								cust.getId(), blb
								);
						String code = dao.createBookingCode(booking.getId());
						int pin = Command.createPinFromCode(code);
						Timer emailThread = new Timer();
						emailThread.schedule(new MailSender(email, firstName, code, pin, request), 0);
						
						
						nextPage = "confirmation.jsp";
					} else {
						BookingDTO booking=dao.getCustomerBookingFromCode(codehidden);
						for (BookingSelection bs : blb.getList()) {
							dao.addRoomSchedule(booking.getId(), 
									bs.getRoomType(), 
									blb.getLocation(), 
									blb.getStartYear()+"-"+blb.getStartMonth()+"-"+blb.getStartDay(), 
									blb.getEndYear()+"-"+blb.getEndMonth()+"-"+blb.getEndDay(), bs.isExtraBed()
									);
						}
						request.setAttribute("URLhidden", codehidden);
						nextPage = "confirmation.jsp";
					}
				}
			} else {
			}
		}

		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

}
