package edu.unsw.comp9321;

import java.util.LinkedList;

public class Cart {
	
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
