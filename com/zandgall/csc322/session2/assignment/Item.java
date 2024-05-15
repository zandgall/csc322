/* CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Item
 # Stores a title and price for an abstract Item.

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

public abstract class Item {
	private String title;
	private double price;

	public Item() {}

	public String getTitle() { return title; }

	public double getPrice() { return price; }
	
	public void setTitle(String title) { this.title = title; }

	public void setPrice(double price) { this.price = price; }

	/**
	* Gets overridden by subclasses for {@link OnlineStore#showItems() showItems}
	*/
	public String getType() { return "Item"; }
}
