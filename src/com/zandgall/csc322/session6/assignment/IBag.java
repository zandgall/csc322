/* CSC322 SESSION 6: ASSIGNMENT - PROF. SUSAN FURTNEY
 > I certify, that this computer program submitted by me is all of my own work.
 > ZANDER GALL - GALLA@CSP.EDU

 ## IBag
 # An interface that lets you add and remove items from a 'bag', 
 # With methods to check if it's empty or contains an element

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.assignment;

public interface IBag<E> {
	
	public void add(E item);

	public E remove();

	public boolean contains(E item);

	public boolean empty();

}

