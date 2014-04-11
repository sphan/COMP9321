package edu.unsw.comp9321;

import java.util.LinkedList;

public class Cart implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public Cart() {
		items = new LinkedList<Stock>();
	}
	
	public void addToCart(Stock item) {
		items.add(item);
	}
	
	public LinkedList<Stock> getItems() {
		return this.items;
	}
	
	public int getCartSize() {
		return items.size();
	}
	
	public void removeItem(Stock item) {
		items.remove(item);
	}

	private LinkedList<Stock> items;
}
