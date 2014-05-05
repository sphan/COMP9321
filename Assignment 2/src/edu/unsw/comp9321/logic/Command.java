package edu.unsw.comp9321.logic;

import javax.servlet.http.HttpServletRequest;

import edu.unsw.comp9321.jdbc.DAO;
import edu.unsw.comp9321.jdbc.StaffDTO;
import edu.unsw.comp9321.jdbc.StaffType;

public class Command {
	public static String search(HttpServletRequest request, DAO dao) {
		String nextPage = "";
		
		return nextPage;
	}
	
	public static String login(HttpServletRequest request, DAO dao) {
		String nextPage = "customerMain.jsp";
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		StaffDTO staff = dao.getStaffByUsername(username);
		if (staff != null) {
			if (verifyLogin(staff, password)) {
				if (staff.getType() == StaffType.MANAGER)
					nextPage = "staffPage.jsp";
				request.setAttribute("staffName", staff.getName());
			}
		}
		
		return nextPage;
	}
	
	/***********************************************
	 * HELPER FUNCTIONS
	 **********************************************/
	public static boolean verifyLogin(StaffDTO staff, String password) {
		if (staff.getPassword().equals(password))
			return true;
		return false;
	}
}
