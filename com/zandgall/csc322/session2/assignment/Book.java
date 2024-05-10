/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Book
 # Simple class that holds book data for an inventory.

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

public class Book extends Item {

	private final String authors, edition, publishers;
	private final int pubYear;

	public Book(String title, String authors, String edition, String publishers, int pubYear, float price) {
		super(title, price);
		this.authors = authors;
		this.edition = edition;
		this.publishers = publishers;
		this.pubYear = pubYear;
	}

	public String getAuthors() { return authors; }
	public String getEdition() { return edition; }
	public String getPublisher() { return publishers; }
	public int getPubYear() { return pubYear; }

	@Override
	public String getType() { return "Book"; }
}

