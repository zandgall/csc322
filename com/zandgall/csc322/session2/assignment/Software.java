/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Software
 # A simple class that stores information about a Software entry in an inventory

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

public class Software extends Item {
	private final String version;

	public Software(String title, String version, float price) {
		super(title, price);
		this.version = version;
	}

	public String getVersion() { return version; }

	@Override
	public String getType() { return "Software"; }
}
