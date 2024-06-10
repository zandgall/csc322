/* CSC322 SESSION 6: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Unsorted Bag
 # A generic implementation of "IBag" that wraps an ArrayList

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
		int lower = 0, upper = data.size()-1, mid = (lower + upper) / 2;
		while(lower <= upper) {
			int result = data.get(mid).compareTo(item);
			// Used to track the binary search process
			// System.out.printf("Checking %d (%d %d) to get %s%n", mid, lower, upper, data.get(mid).toString());
			if(result == 0)
				return true;
			else if(result > 0)
				upper = mid - 1;
			else
				lower = mid + 1;
			mid = (lower + upper) / 2;
		}
		return false;
	}

	public boolean empty() { return data.isEmpty(); }

}
