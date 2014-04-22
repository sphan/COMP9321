package edu.unsw.comp9321;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");
		 /*
		  * Our default page
		  */
		 String nextPage = "enough.jsp";
		 if(action.equals("menu"))
			 nextPage = "menu.jsp";
		 else if(action.equals("add"))
		 	  {
			 	String state = request.getParameter("state");
			 	JourneyBean journey = (JourneyBean) request.getSession().getAttribute("journey");
			     Vector<String> places = journey.getPlaces();
			     if(places.contains(state))
			    	 	request.setAttribute("isAlreadySelected", true);
			     else{
			    	 		request.setAttribute("isAlreadySelected", false);
			    	 		places.add(state);
			    	 		journey.setPlaces(places);
			     	 }
			     nextPage = "itinerary.jsp";
		 	  }
		 else if(action.equals("enough")){
			 	request.getSession().invalidate();
			 	nextPage = "enough.jsp";
	     }
		 /*
		  * Go to whatever has been selected as the next page.
		  */
		 RequestDispatcher rd = request.getRequestDispatcher("/"+nextPage);
		 rd.forward(request, response);
	     //response.addCookie(new Cookie("Currency","AUD"));
	}

}
