package edu.unsw.comp9321;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet displays a menu of the destinations (state)
 * for the traveller.
 * 
 * By clicking on submit, you are sent to 'control' 
 * or the ControlServlet
 */
@WebServlet(name="MenuServlet",urlPatterns="/menu")
public class MenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MenuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		PrintWriter out = response.getWriter(); 
		Journey jny;
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("JourneyFlag") == null) 
		{ 
			jny= new Journey(); 
			session.setAttribute("JourneyFlag",jny);
		}
		out.println("<HTML>"); 
		out.println("<BODY>"); 
		out.println("<CENTER>"); 
		out.println("<H1>Choose a state</H1>"); 
		out.println("<FORM	Action='control' METHOD='POST'>");
		out.println("<SELECT NAME='State'   SIZE=7>");
		out.println("<OPTION> ACT");
		out.println("<OPTION> NSW");
		out.println("<OPTION> SA");
		out.println("<OPTION> TAS");
		out.println("<OPTION> VIC");
		out.println("<OPTION> WA");
		out.println("<OPTION> NT");
		out.println("<OPTION> QLD");
		out.println("</SELECT>"); 
		out.println("<BR/><BR/><INPUT type='submit' value='Send'>"+ "<INPUT type='reset'>"); 
		out.println("</FORM>");
		out.println("</CENTER>"); 
		out.println("</BODY>"); 
		out.println("</HTML>"); 
	}

}
