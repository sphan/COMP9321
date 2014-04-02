package edu.unsw.comp9321;

import java.util.*; 

import java.io.Serializable;

/**
 * 
 */
public class Journey implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	private Vector<String> places;

	public Journey() { places = new Vector<String>(); } 
	public Iterator<String> getPlaces() { return this.places.iterator(); } 
	public boolean addPlace(String place) { return places.add(place); }
	
	/**
	 * Overrode the toString() method to so that the Object.toString()
	 * returns the itinerary stored in this object 	
	 */
	public String toString() { 
		String retVal = "";
		for(String place:places){
			retVal.concat(place+" ");
		}
		return "Journey to "+retVal;
	}
}