package edu.unsw.comp9321;

public class Song implements java.io.Serializable, Stock {
	private static final long serialVersionUID = 1L;
	
	public Song(String title) {
		this.title = title;
		this.artist = "";
		this.albumID = "";
		this.price = 0;
		this.songID = "";
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getAlbumID() {
		return albumID;
	}
	
	public void setAlbumID(String albumID) {
		this.albumID = albumID;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getSongID() {
		return songID;
	}
	
	public void setSongID(String songID) {
		this.songID = songID;
	}

	@Override
	public StockType getType() {
		return type;
	}

	private String artist;
	private String title;
	private String publisher;
	private String albumID;
	private float price;
	private String songID;
	private final StockType type = StockType.SONG;

}
