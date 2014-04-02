package edu.unsw.comp9321;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet displays a welcome page to the user
 * 
 * By clicking submit, the user is sent to the Menu page.
 */
@WebServlet(name="WelcomeServlet",urlPatterns="/welcome")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WelcomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException	{ 
    	res.setContentType("text/html"); 
    	PrintWriter out = res.getWriter();
    	out.println("<HTML>"); out.println("<BODY>"); 
    	out.println("<CENTER>"); out.println("<H1>Let's visit Australia!</H1>"); 
    	out.println("Would you like to begin your journey?"); 
    	out.println("<FORM ACTION='menu' METHOD='POST'>"); 
    	out.println("<INPUT TYPE='submit' VALUE='Onwards!'>"); 
    	out.println("</FORM>");
    	out.println("</CENTER>"); out.println("</BODY>"); out.println("</HTML>"); 
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
