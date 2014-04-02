package edu.unsw.comp9321;

import java.util.*; 

import java.io.Serializable;

public class Journey implements Serializable { 
	private Vector Places;

	public Journey() { Places = new Vector(); } 
	public Iterator getPlaces() { return this.Places.iterator(); } 
	public boolean addPlace(String place) { return Places.add(place); } 
	public String toString() { return "Journey to "; }
}