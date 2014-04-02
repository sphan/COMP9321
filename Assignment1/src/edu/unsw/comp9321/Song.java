package edu.unsw.comp9321;

public class Song {
	
	public Song(String title, float price, String songID) {
		this.title = title;
		this.artist = "";
		this.albumID = "";
		this.price = price;
		this.songID = songID;
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

	private String artist;
	private String title;
	private String albumID;
	private float price;
	private String songID;	
}
