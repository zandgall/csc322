/* CSC322 SESSION 6: ASSIGNMENT - PROF. SUSAN FURTNEY
 > I certify, that this computer program submitted by me is all of my own work.
 > ZANDER GALL - GALLA@CSP.EDU

 ## Sorted Bag
 # An implementation of "IBag" that wraps an ArrayList and sorts it,
 # Also uses binary search to see if an item is in the bag

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.assignment;

import java.util.ArrayList;

public class SortedBag<E extends Comparable<E>> implements IBag<E> {
	private ArrayList<E> data = new ArrayList<E>();
	public SortedBag() {}

	public void add(E item) {
		data.add(item);
		data.sort(null);
	}

	public E remove() { return data.remove(0); }

	public boolean contains(E item) {
		// Set lower and upper bounds, and check middle
		int lower = 0, upper = data.size()-1, mid = (lower + upper) / 2;

		// Repeat until lower bound passes/passed by upper bound
		while(lower <= upper) {
			// Get data and use "String.compareTo" to see if it's lower or higher 
			// than the item we're looking for
			int result = data.get(mid).compareTo(item);
			// If the item is the same as what's at data[mid], return found true
			if(result == 0)
				return true;
			// If data[mid] is lower than 'item',
			else if(result > 0)
				upper = mid - 1; // constrain upper bound
			else
				lower = mid + 1; // otherwise constrain lower bound
			// update mid value
			mid = (lower + upper) / 2;
		}
		// Couldn't find it!
		return false;
	}

	public boolean empty() { return data.isEmpty(); }
}
