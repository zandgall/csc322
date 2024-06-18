/* CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Book
 # Simple class that holds book data for an inventory.

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

public class Book extends Item {

	private final String authors, edition, publisher;
	private final int pubYear;

	public Book(String authors, String publisher, String edition, int pubYear) {
		this.authors = authors;
		this.edition = edition;
		this.publisher = publisher;
		this.pubYear = pubYear;
	}

	public String getAuthors() { return authors; }
	public String getEdition() { return edition; }
	public String getPublisher() { return publisher; }
	public int getPubYear() { return pubYear; }

	@Override
	public String getType() { return "Book"; }
}

