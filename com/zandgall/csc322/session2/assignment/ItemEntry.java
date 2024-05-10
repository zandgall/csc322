/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ItemEntry
 #

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

public class ItemEntry {

	private final Item item;
	private int quantity;

	public ItemEntry(Item item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public int getQuantity() { return quantity; }

	public void setQuantity(int quantity) { this.quantity = quantity; }

	public Item getItem() { return item; }

}
