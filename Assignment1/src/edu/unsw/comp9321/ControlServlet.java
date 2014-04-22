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
			if (a.getTitle().contains(searchString)) {
				results.add(a);
			}
		}
		return results;
	}
	
	/**
	 * Search for the requested items from the searching string.
	 * @param request The HttpServletRequest that contains all the 
	 *     request in the page.
	 * @return The name of the JSP page to direct to.
	 */
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
	
	/**
	 * Add the selected items into cart.
	 * @param request The HttpServletRequest that contains the items to 
	 *     be added.
	 * @return The name of the JSP page that is to be directed to.
	 */
	public String add(HttpServletRequest request) {
		Cart myCart = (Cart) request.getSession().getAttribute("cart");
		HashMap<String, HashMap<StockType, LinkedList<Stock>>> itemsAlreadyInCart = new HashMap<String, HashMap<StockType, LinkedList<Stock>>>();
		
		String[] itemsToAdd = request.getParameterValues("addToCart");
		if (itemsToAdd == null) {
			request.setAttribute("cartSize", myCart.getCartSize());
			return "cart.jsp";
		}
		
		for (String item : itemsToAdd) {
			// if item is not in cart.
			LinkedList<Stock> inCart = itemIsInCart(item, myCart.getItems()); 
			if (inCart == null) {
				System.out.println(item);
				myCart.addToCart(getItem(item));
			} else {
				HashMap<StockType, LinkedList<Stock>> duplicated = new HashMap<StockType, LinkedList<Stock>>();
				duplicated.put(getItem(item).getType(), inCart);
				itemsAlreadyInCart.put(item, duplicated);
			}
		}
		request.setAttribute("cartSize", myCart.getCartSize());
		request.setAttribute("totalCost", getTotal(myCart));
		request.setAttribute("alreadyInCartSize", itemsAlreadyInCart.size());
		request.setAttribute("alreadyInCart", itemsAlreadyInCart);
		return "cart.jsp";
	}
	
	/**
	 * Remove the selected items in cart.
	 * @param request The HttpServletRequest that contains the items
	 *     to be removed.
	 * @return The name of the JSP page to be directed to.
	 */
	public String remove(HttpServletRequest request) {
		Cart myCart = (Cart) request.getSession().getAttribute("cart");
		LinkedList<Stock> itemsInCart = myCart.getItems();
		
		String[] itemsToRemove = request.getParameterValues("removeFromCart");
		if (itemsToRemove == null)
			return "cart.jsp";
		
		for (String item : itemsToRemove) {
			itemsInCart.remove(getItem(item));
		}
		request.setAttribute("cartSize", myCart.getCartSize());
		request.setAttribute("totalCost", getTotal(myCart));
		
		return "cart.jsp";
	}
	
	/**
	 * Check out the items that the user had added into cart
	 * and display the list of items without check boxes. Users cannot
	 * remove items on this page.
	 * @param request The HttpServletRequest
	 * @return The next page to be directed to.
	 */
	public String checkout(HttpServletRequest request) {
		Cart myCart = (Cart) request.getSession().getAttribute("cart");
		float totalPrice = 0;
		
		for (Stock s : myCart.getItems()) {
			totalPrice += s.getPrice();
		}
		request.setAttribute("totalCost", totalPrice);
		return "confirm.jsp";
	}
	
	/**
	 * Leave the website and invalidate sessions.
	 * @param request The HttpServletRequest.
	 * @param action The action taken buy the user whether
	 *     to buy or to leave without buy anything.
	 * @return The next page that is to be directed to.
	 */
	public String leave(HttpServletRequest request, String action) {
		request.setAttribute("action", action);
		request.getSession().invalidate();
		return "end.jsp";
	}
	
	/**
	 * Get total cost of purchase in cart.
	 * @param myCart The cart.
	 * @return The total cost.
	 */
	public float getTotal(Cart myCart) {
		float total = 0;
		for (Stock s : myCart.getItems()) {
			total += s.getPrice();
		}
		return total;
	}
	
	/**
	 * Search for the list of songs that matches the searchString.
	 * @param searchString The searchString that is typed into the
	 * 	   text box in the page.
	 * @return The list of songs grouped into Hash Table for easy access.
	 */
	public HashMap<Album, LinkedList<Song>> searchForSong(String searchString) {
		HashMap<Album, LinkedList<Song>> results = new HashMap<Album, LinkedList<Song>>();
		
		// Traverse the database, inside each album, looks for the song
		// that contains the search string.
		for (Album a : musicDb) {
			LinkedList<Song> songs = new LinkedList<Song>();
			for (Song s : a.getSongs()) {
				if (s.getTitle().contains(searchString)) {
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
	 * Given the title of the item, get the Stock object of the item.
	 * @param item The title of the item.
	 * @return The Stock object of the item.
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
		for (Album album : musicDb) {
			if (album.getAlbumID().equalsIgnoreCase(song.getAlbumID())) {
				return album;
			}
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
	public LinkedList<Stock> itemIsInCart(String item, LinkedList<Stock> cart) {
		if (cart.size() == 0) {
			System.out.println("Cart is empty");
			return null;
		}
		LinkedList<Stock> duplicated = new LinkedList<Stock>();
		
		for (Stock s : cart) {
			// If the same song is found in cart.
			if (item.equalsIgnoreCase(s.getTitle()))
				duplicated.add(s);
			
			// Find in cart to see if the songs in this album
			// is already added.
			else if (s.getType() == StockType.ALBUM) {
				Album album = (Album) s;
				for (Song song : album.getSongs()) {
					if (song.getTitle().equalsIgnoreCase(item))
						duplicated.add(s);						
				}
			} else if (s.getType() == StockType.SONG) {
				Song song = (Song) s;
				if (getSongAlbum(song).getTitle().equalsIgnoreCase(item)) {
					System.out.println(item);
					duplicated.add(s);
				}
			}
		}
		return duplicated;
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
		
		// Get the pages to be directed to next depending
		// on the 'action' and what each function returns.
		if (action.equals("search")) {			
			nextPage = search(request);
		} else if (action.equals("add")) {
			nextPage = add(request);
		} else if (action.equals("remove")) {
			nextPage = remove(request);
		} else if (action.equals("checkout")) {
			nextPage = checkout(request);
		} else if (action.equals("buy") || action.equals("leave")) {
			nextPage = leave(request, action);
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
