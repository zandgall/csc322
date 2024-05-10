/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Main
 # Wrapper for OnlineStore.start

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, ItemInventory.InvalidTypeException {
		new OnlineStore().start();
	}
}

