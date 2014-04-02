package edu.unsw.comp9321;

import java.util.LinkedList;

public class Album {
	
	public Album(String title, float price, String id) {
		this.artist = "";
		this.title = title;
		this.albumID = id;
		this.price = price;
		this.publisher = "";
		this.year = 1920;
		this.songs = new LinkedList<Song>();
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
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public LinkedList<Song> getSongs() {
		return songs;
	}
	
	public void addSong(Song song) {
		this.songs.add(song);
	}

	private String artist;
	private String title;
	private String albumID;
	private String publisher;
	private int year;
	private float price;
	private LinkedList<Song> songs;
}
