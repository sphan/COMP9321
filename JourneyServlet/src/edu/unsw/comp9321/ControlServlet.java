package edu.unsw.comp9321;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The ControlServlet adds the user selection to the Journey object 
 * which is linked to the user's session.
 * 
 * The it displays the list of places added by the user 
 */
@WebServlet(name="ControlServlet",urlPatterns="/control")
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		PrintWriter out = response.getWriter();
		out.println("<HTML>"); 
		out.println("<BODY>"); 
		out.println("<CENTER>"); 
		String state = request.getParameter("State"); 
		out.println("<H1>Thanks for choosing "+state+"</H1>"); 
		HttpSession session = request.getSession(true);
		Journey jny = (Journey)session.getAttribute("JourneyFlag"); 
		jny.addPlace(state); 
		Iterator iter = jny.getPlaces(); 
		out.println("Your journey so far: ");
		while (iter.hasNext()) out.println("- "+ iter.next()); 
		out.println("<BR/>"); 
		out.println("Would you like to continue your travels?"); 
		out.println("<FORM ACTION='menu' METHOD='POST'>"); 
		out.println("<INPUT TYPE='submit' VALUE='More!'>"); 
		out.println("</FORM>");
		out.println("<FORM ACTION='enough' METHOD='POST'>"); 
		out.println("<INPUT TYPE='submit' VALUE='Enough!'>"); 
		out.println("</FORM>"); 
		out.println("</CENTER>");
		out.println("</BODY>"); 
		out.println("</HTML>");
		out.close();
	}

}
