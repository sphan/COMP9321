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
 * This servlet ends the user session and allows the user to go back
 * to the Welcome page.
 */
@WebServlet(name="EnoughServlet",urlPatterns="/enough")
public class EnoughServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnoughServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		PrintWriter out = response.getWriter(); 
		HttpSession session = request.getSession(true); 
		session.invalidate(); 
		out.println("<HTML>"); 
		out.println("<BODY>"); 
		out.println("<CENTER>"); 
		out.println("<H1>No longer in session!</H1>"); 
		out.println("<FORM ACTION='welcome' METHOD='GET'>"); 
		out.println("<INPUT type='submit' value='Try it'>"); 
		out.println("</FORM>");
		out.println("</CENTER>"); 
		out.println("</BODY>"); 
		out.println("</HTML>");
	}

}
