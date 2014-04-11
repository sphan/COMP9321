package edu.unsw.comp9321;

import java.io.IOException;
import java.util.HashMap;
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
	
	public String search(HttpServletRequest request) {
		String searchType = request.getParameter("searchType");
		String searchString = request.getParameter("searchString");
		
		// Only look up for albums with similar names if user chose
		// to only look for albums.
		if (searchType.equals("Album")) {
			LinkedList<Album> albumsFound = searchForAlbum(searchString);
			
			// Display 'No result' message.
			if (albumsFound.size() == 0) {
				return "noResults.jsp";
			} else {
				request.setAttribute("albumsFound", albumsFound);
				return "albumResults.jsp";
			}
			
		// Look up for songs and the album that it belongs to.
		} else if (searchType.equals("Song")) {
			HashMap<Album, LinkedList<Song>> songsFound = searchForSong(searchString);
			
			// Display 'No result' message.
			if (songsFound.size() == 0) {
				return "noResults.jsp";
			} else {
				request.setAttribute("songsFound", songsFound);
				return "songResults.jsp";
			}
		}
		return null;
	}
	
	public String add(HttpServletRequest request) {
		Cart myCart = (Cart) request.getSession().getAttribute("cart");
		LinkedList<Stock> itemsInCart = myCart.getItems();
		
		String[] itemsToAdd = request.getParameterValues("addToCart");
		
		for (String item : itemsToAdd) {
			// if item is not in cart.
			if (!itemIsInCart(item, itemsInCart)) {
				myCart.addToCart(getItem(item));
				System.out.println(item + " added to cart.");
			}
		}
		System.out.println(myCart.getCartSize());
		return "cart";
	}
	
	/**
	 * 
	 * @param searchString
	 * @return
	 */
	public HashMap<Album, LinkedList<Song>> searchForSong(String searchString) {
		HashMap<Album, LinkedList<Song>> results = new HashMap<Album, LinkedList<Song>>();
		
		// Traverse the database, inside each album, looks for the song
		// that contains the search string.
		for (Album a : musicDb) {
			LinkedList<Song> songs = new LinkedList<Song>();
			for (Song s : a.getSongs()) {
				if (s.getTitle().toLowerCase().contains(searchString.toLowerCase())) {
					songs.add(s);
				}
			}
			results.put(a, songs);
		}
		
		return results;
	}
	
	/**
	 * Check if a given song belongs to the given album.
	 * @param song The name of the song.
	 * @param album The title of the album.
	 * @return True if the song belongs to the album. False otherwise.
	 */
	public boolean songBelongsToAlbum(String song, String album) {
		for (Album a : musicDb) {
			if (a.getTitle().equalsIgnoreCase(album)) {
				for (Song s : a.getSongs()) {
					if (s.getTitle().equalsIgnoreCase(song))
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public Stock getItem(String item) {
		for (Album a : musicDb) {
			if (a.getTitle().equalsIgnoreCase(item))
				return a;
			for (Song s : a.getSongs()) {
				if (s.getTitle().equalsIgnoreCase(item))
					return s;
			}
		}
		return null;
	}
	
	/**
	 * Find the album in which the song belongs to.
	 * @param song The song.
	 * @return Return the album which the song belongs to. Or
	 * return null if no album is found.
	 */
	public Album getSongAlbum(Song song) {
		for (Album album :musicDb) {
			if (album.getAlbumID() == song.getAlbumID())
				return album;
		}
		return null;
	}
	
	/**
	 * Determine if the item given is already in cart. An item is in
	 * the cart if there appears to have an item with the same name or
	 * item is a song and the album which the song belongs to is already
	 * in the card, or the album about to be added already have a song added
	 * in the cart.
	 * @param item The item to be added to the cart.
	 * @param cart The user's cart.
	 * @return True if the item is already in the cart. False otherwise.
	 */
	public boolean itemIsInCart(String item, LinkedList<Stock> cart) {
		if (cart.size() == 0)
			return false;
		
		for (Stock s : cart) {
			// If the same song is found in cart.
			if (item.equalsIgnoreCase(s.getTitle()))
				return true;
			
			// Find in cart to see if the songs in this album
			// is already added.
			else if (s.getType() == StockType.ALBUM) {
				Album album = (Album) getItem(item);
				for (Song song : album.getSongs()) {
					// A song in the album is already added to cart.
					if (song.getTitle() == s.getTitle())
						return true;
				}
				
			// Find if the album in which the item (song) belongs to
			// is already added into cart.
			} else {
				Song song = (Song) getItem(item);
				Album myAlbum = getSongAlbum(song);
				if (myAlbum.getTitle() == s.getTitle())
					return true;
			}
		}
		return false;
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
		String nextPage = "";
		musicDb = (LinkedList<Album>) System.getProperties().get("musicDb");
		
		// Display the result pages if the user wants to searched for
		// something.
		if (action.equals("search")) {			
			nextPage = search(request);
		} else if (action.equals("add")) {
			nextPage = add(request);
		} else {
			response.sendRedirect("/Assignment1/welcome");
			return;
		}
		
		// Go to whatever has been selected as the next page.
		RequestDispatcher rd = request.getRequestDispatcher("/" + nextPage);
		rd.forward(request, response);
	}

	private LinkedList<Album> musicDb;
}
