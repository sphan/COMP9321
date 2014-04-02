package edu.unsw.comp9321;

import java.util.ArrayList;
import java.util.List;

public class LocationBean{
	private List<String> destinations;
	
	public LocationBean(){
		destinations=new ArrayList<String>();
	}
	
	public List<String> getDestinations(){
		return destinations;
	}
	
	public void setDestinations(ArrayList<String> o){
		destinations = o;
	}
}
