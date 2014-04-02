package edu.unsw.comp9321;

import java.util.*; 

import java.io.Serializable;

/*
 * This keeps a track of the person's chosen itinerary
 */
public class JourneyBean implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<String> places;

	public JourneyBean() { 
		places = new Vector<String>(); 
	} 
	
	public Vector<String> getPlaces() { 
		return places; 
	} 
	
	public void setPlaces(Vector<String> places) {
		this.places = places;
	}
	
	public String toString() { return "JourneyBean to "; }
}