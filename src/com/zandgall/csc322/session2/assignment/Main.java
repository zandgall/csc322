/* CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Main
 # Wrapper for OnlineStore.start
 # Throws more exceptions than just IOException in order to account for additional details added in my implementation.

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.io.IOException;
import java.text.ParseException;

public class Main {
	public static void main(String[] args) throws IOException, ItemInventory.InvalidTypeException, ParseException {
		new OnlineStore().start();
	}
}

