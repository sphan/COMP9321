package edu.unsw.comp9321;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet(urlPatterns="/control",displayName="ControlServlet")
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ControlServlet() {
		super();
		this.musicDb = new LinkedList<Album>();
	}
	
	/**
	 * Search in database for the album with matching substring.
	 * @param searchString The substring in which the user typed in
	 *     that is to be used in searching for the matching albums.
	 * @return The list of albums with matching substring.
	 */
	public LinkedList<Album> searchForAlbum(String searchString) {
		LinkedList<Album> results = new LinkedList<Album>();
		
		// Traverse the database and look for the album that contains
		// the search string.
		for (Album a : musicDb) {
			if (a.getTitle().toLowerCase().contains(searchString.toLowerCase())) {
				results.add(a);
			}
		}
		return results;
	}
	
	/**
	 * 
	 * @param searchString
	 * @return
	 */
	public LinkedList<Song> searchForSong(String searchString) {
		LinkedList<Song> results = new LinkedList<Song>();
		
		// Traverse the database, inside each album, looks for the song
		// that contains the search string.
		for (Album a : musicDb) {
			for (Song s : a.getSongs()) {
				if (s.getTitle().toLowerCase().contains(searchString.toLowerCase())) {
					results.add(s);
				}
			}
		}
		
		return results;
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		// Always direct back to home page.
		String nextPage = "welcome.jsp";
		musicDb = (LinkedList<Album>) request.getAttribute("musicDb");
		
		if (action.equals("search")) {
			String searchType = request.getParameter("searchType");
			String searchString = request.getParameter("searchString");
			if (searchType.equals("Album")) {
				LinkedList<Album> results = searchForAlbum(searchString);
			} else if (searchType.equals("Song")) {
				
			}
		}
		
		// Go to whatever has been selected as the next page.
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

	private LinkedList<Album> musicDb;
}
