package edu.unsw.comp9321;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This servlet displays a welcome page to the user
 * 
 * By clicking submit, the user is sent to the Menu page.
 */
@WebServlet(name="WelcomeServlet",urlPatterns="/welcome")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WelcomeServlet() {
		super();
	}
	
	public boolean validateXMLSchema(String xsdPath, String xmlPath) {
		try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (Exception e) {
            System.out.println("Exception: "+ ((Throwable) e).getMessage());
            return false;
        }
        return true;
	}
	
	/**
	 * Creates a music database using a linked list to store
	 * all the albums read from the XML file.
	 * @return The music database.
	 */
	public LinkedList<Album> getMusicDB() {
		if (validateXMLSchema("C:\\Users\\San\\git\\COMP9321\\Assignment1\\WebContent\\WEB-INF\\musicDb.xsd",
				"C:\\Users\\San\\git\\COMP9321\\Assignment1\\WebContent\\WEB-INF\\musicDb.xml") == false) {
			System.out.println("XML file is not valid.");
			return null;
		}
		
		LinkedList<Album> musicDb = new LinkedList<Album>();
		
		// DOM XML Parser tutorial from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("C:\\Users\\San\\git\\COMP9321\\Assignment1\\WebContent\\WEB-INF\\musicDb.xml"));
			
			// Removes unnecessary spaces and new lines in the text
			// in the HTML tags.
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("albumList");
			
			// Loop through the list of album list and add the albums
			// from XML into musicDb.
			for (int temp = 0; temp < nList.getLength(); ++temp) {
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					addAlbumToDB(musicDb, element);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return musicDb;
	}
	
	/**
	 * 
	 * @param db
	 * @param element
	 */
	public void addAlbumToDB(LinkedList<Album> db, Element element) {
		Album album = new Album(element.getElementsByTagName("title").item(0).getTextContent());
		album.setAlbumID(element.getElementsByTagName("ID").item(0).getTextContent());
		album.setArtist(element.getElementsByTagName("artist").item(0).getTextContent());
		album.setGenre(element.getElementsByTagName("genre").item(0).getTextContent());
		album.setPublisher(element.getElementsByTagName("publisher").item(0).getTextContent());		
		addSongsToAlbum(album, element);
		
		album.setYear(Integer.parseInt(element.getElementsByTagName("year").item(0).getTextContent()));
		album.setPrice(Float.parseFloat(element.getElementsByTagName("price").item(0).getTextContent()));
		
		db.add(album);
	}
	
	/**
	 * 
	 * @param album
	 * @param albumElm
	 */
	public void addSongsToAlbum(Album album, Element albumElm) {
		NodeList nList = albumElm.getElementsByTagName("songList");
		
		for (int i = 0; i < nList.getLength(); ++i) {
			Node node = nList.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element songElm = (Element) node;
				
				Song song = new Song(songElm.getElementsByTagName("title").item(0).getTextContent());
				song.setArtist(songElm.getElementsByTagName("artist").item(0).getTextContent());
				song.setAlbumID(songElm.getElementsByTagName("albumID").item(0).getTextContent());
				song.setPrice(Float.parseFloat(songElm.getElementsByTagName("price").item(0).getTextContent()));
				song.setSongID(songElm.getElementsByTagName("songID").item(0).getTextContent());
				
				album.addSong(song);
			}
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Read the albums from the XML file and store it as a DB that is going
    	// to be passed around the servlets.
    	LinkedList<Album> musicDb = getMusicDB();
    	
    	System.getProperties().put("musicDb", musicDb);
    	
    	// Go to welcome page.
		RequestDispatcher rd = request.getRequestDispatcher("/welcome.jsp");
		rd.forward(request, response);
    }
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
