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
import edu.unsw.comp9321.jdbc.*;

/**
 * Servlet implementation class Default
 */
@WebServlet("/Default")
public class DefaultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DefaultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession().setAttribute("URL", null);
		PassByRef pbr = new PassByRef();
		SearchDetailsBean sdb = (SearchDetailsBean) request.getSession().getAttribute("searchDetails");
		if (sdb == null) {
			//session expired, create new sdb
			sdb = new SearchDetailsBean();
			request.getSession().setAttribute("searchDetails", sdb);
		}
		
		List<PeakPeriodDTO> peakPeriods = new ArrayList<PeakPeriodDTO>();
		peakPeriods.add(new PeakPeriodDTO(15, Calendar.DECEMBER, 15, Calendar.FEBRUARY));
		peakPeriods.add(new PeakPeriodDTO(25, Calendar.MARCH, 14, Calendar.APRIL));
		peakPeriods.add(new PeakPeriodDTO(1, Calendar.JULY, 20, Calendar.JULY));
		peakPeriods.add(new PeakPeriodDTO(20, Calendar.SEPTEMBER, 10, Calendar.OCTOBER));
		
		request.getSession().setAttribute("peakPeriods", peakPeriods);
		
		pbr.postErrorMessage(request);
		RequestDispatcher rd = request.getRequestDispatcher("/" + "customerMain.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
