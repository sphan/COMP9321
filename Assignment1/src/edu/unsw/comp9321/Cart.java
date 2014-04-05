package edu.unsw.comp9321;

import java.util.LinkedList;

public class Cart {
	
	public Cart() {
		stocks = new LinkedList<Stock>();
	}
	
	public void addToCart(Stock item) {
		stocks.add(item);
	}
	
	public LinkedList<Stock> getStocks() {
		return this.stocks;
	}
	
	public int getCartSize() {
		return stocks.size();
	}
	
	public void removeItem(Stock item) {
		stocks.remove(item);
	}

	private LinkedList<Stock> stocks;
}
