package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321.bean.SearchDetailsBean;
import edu.unsw.comp9321.bean.URLBookingBean;
import edu.unsw.comp9321.jdbc.BookingDTO;
import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.PeakPeriodDTO;
import edu.unsw.comp9321.jdbc.RoomTypeDTO;

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

	public void updatePeakPeriodPrice(List<RoomTypeDTO> roomTypeList) {
		for (RoomTypeDTO rtl : roomTypeList) {
			rtl.setPrice(rtl.getPrice() + (40 * rtl.getPrice() / 100));
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getRequestURI().replaceAll(".*\\/", ""); // get the code from url
		URLBookingBean ubb = (URLBookingBean) request.getSession().getAttribute("URL");
		if (ubb == null) {
			ubb = new URLBookingBean();
			request.getSession().setAttribute("URL", ubb);
		}
		
		SearchDetailsBean sdb = (SearchDetailsBean) request.getSession().getAttribute("searchDetails");
		if (sdb == null) {
			//session expired, create new sdb
			sdb = new SearchDetailsBean();
			request.getSession().setAttribute("searchDetails", sdb);
		}
		
		PassByRef pbr = new PassByRef();
		DAO dao = new DAO(pbr);
		String nextPage;

		BookingDTO booking=dao.getCustomerBookingFromCode(code);
		sdb.setStartDay(booking.getStartDay());
		sdb.setStartMonth(booking.getStartMonth());
		sdb.setStartYear(booking.getStartYear());
		sdb.setEndDay(booking.getEndDay());
		sdb.setEndMonth(booking.getEndMonth());
		sdb.setEndYear(booking.getEndYear());
		sdb.setLocation(booking.getHotel().getLocation());

		if (booking != null) {

			List<PeakPeriodDTO> peakPeriods = new ArrayList<PeakPeriodDTO>();
			peakPeriods.add(new PeakPeriodDTO(15, Calendar.DECEMBER, 15, Calendar.FEBRUARY));
			peakPeriods.add(new PeakPeriodDTO(25, Calendar.MARCH, 14, Calendar.APRIL));
			peakPeriods.add(new PeakPeriodDTO(1, Calendar.JULY, 20, Calendar.JULY));
			peakPeriods.add(new PeakPeriodDTO(20, Calendar.SEPTEMBER, 10, Calendar.OCTOBER));

			request.getSession().setAttribute("peakPeriods", peakPeriods);
			ubb.setCode(code);
			
			request.setAttribute("bookingDetails", booking);
			nextPage = "bookingInfo.jsp";
		} else {
			pbr.addErrorMessage("The URL entered is invalid");
			nextPage = "";
		}

		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Booking URL search page
		PassByRef pbr = new PassByRef();
		pbr.addURLMessage("Existing Booking");
		String uri = request.getRequestURI().replaceAll(".*\\/", "");
		
		RequestDispatcher rd = request.getRequestDispatcher("/" + uri);
		pbr.postURLMessage(request);
		rd.include(request, response);
	}

}
