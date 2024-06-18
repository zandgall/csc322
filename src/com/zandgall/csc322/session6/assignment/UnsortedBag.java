/* CSC322 SESSION 6: ASSIGNMENT - PROF. SUSAN FURTNEY
 > I certify, that this computer program submitted by me is all of my own work.
 > ZANDER GALL - GALLA@CSP.EDU

 ## Unsorted Bag
 # A generic implementation of "IBag" that wraps an ArrayList

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.assignment;

import java.util.ArrayList;

public class UnsortedBag<E> implements IBag<E> {
	private ArrayList<E> data = new ArrayList<E>();
	public UnsortedBag() {}

	public void add(E item) { data.add(item); }

	public E remove() { return data.remove(0); }

	public boolean contains(E item) { return data.contains(item); }

	public boolean empty() { return data.isEmpty(); }

}

