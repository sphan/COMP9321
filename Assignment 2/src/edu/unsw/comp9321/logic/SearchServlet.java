package edu.unsw.comp9321.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
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
import edu.unsw.comp9321.jdbc.*;

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
	
	public void updatePeakPeriodPrice(List<RoomTypeDTO> roomTypeList) {
		for (RoomTypeDTO rtl : roomTypeList) {
			rtl.setPrice(rtl.getPrice() + (40 * rtl.getPrice() / 100));
		}
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
			
			List<RoomTypeDTO> roomTypeList = dao.getHotelRoomSelection(sdb);
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DATE, sdb.getStartDay());
			startDate.set(Calendar.MONTH, sdb.getStartMonth() - 1);
			startDate.set(Calendar.YEAR, sdb.getStartYear());
			
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.DATE, sdb.getEndDay());
			endDate.set(Calendar.MONTH, sdb.getEndMonth() - 1);
			endDate.set(Calendar.YEAR, sdb.getEndYear());

			// check if selected date is in peak period
			@SuppressWarnings("unchecked")
			List<PeakPeriodDTO> peakPeriods = (List<PeakPeriodDTO>) request.getSession().getAttribute("peakPeriods");
			for (PeakPeriodDTO pp : peakPeriods) {
				// if both start and end dates are during peak period.
				if (pp.isInPeak(startDate, endDate)) {
					
					updatePeakPeriodPrice(roomTypeList);
					break;
				// if only the start day is in peak period.
				} else if (pp.isInPeak(startDate, endDate)) {
					
				// if only the end day is in the peak period.
				} else if (pp.isInPeak(startDate, endDate)) {
					
				}
			}
			request.setAttribute("roomTypeList", roomTypeList);
		}
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + "searchResults.jsp");
		rd.forward(request, response);
	}
}
