/* CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ItemInventory
 # An aggregate of ItemEntries, with a central function to load 

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.io.IOException;
import java.io.File;

public class ItemInventory {

	protected ArrayList<ItemEntry> entries = new ArrayList<ItemEntry>();

	public ItemInventory() {}

	/**
	 * Parse a file to load ItemEntries 
	 * Exceptions are passed up to the {@link Main#main} function
	 */
	public static ItemInventory load(String filename) throws IOException, InvalidTypeException, ParseException {
		// Create necessary objects
		File file = new File(filename);
		Scanner s = new Scanner(file);
		ItemInventory inventory = new ItemInventory();

		// Loop through each line in the file
		while(s.hasNextLine()) {
			String line = s.nextLine();
			String attribs[] = line.split("\\|");
			Item item;

			// The attribs are different for every item type, but the first entries are type and title, and the last entries are price and quantity
			switch(attribs[0]) {
				case "music":
					// Music format is "music|title|artists|release date|label|record company|total length|genres|price|quantity"
					item = new MusicCD(
						attribs[2], // Artists
						OnlineStore.dateFormatter.parse(attribs[3]), // Release Date - throws parse exception
						attribs[4], // Label
						attribs[5], // Record Company
						Integer.parseInt(attribs[6]), // Total Length
						attribs[7]); // Genres
					break;
				case "book":
					// Book format is "book|title|authors|publisher|edition|published year|price|quantity"
					item = new Book(
						attribs[2], // authors
						attribs[3], // publisher
						attribs[4], // edition
						Integer.parseInt(attribs[5])); // Published Year
					break;
				case "software":
					// Software format is "software|title|version|price|quantity"
					item = new Software(attribs[2]); // Version
					break;
				default:
					throw new InvalidTypeException(attribs[0]);
			}

			// Pass generic item information
			item.setTitle(attribs[1]);
			item.setPrice(Float.parseFloat(attribs[attribs.length-2]));

			// Add quantity, create the item entry and add it to the inventory
			inventory.entries.add(new ItemEntry(item, Integer.parseInt(attribs[attribs.length-1]))); 
		}

		return inventory;
	}

	public int size() { return entries.size(); }

	public ItemEntry getEntry(int i) { return entries.get(i); }

	static class InvalidTypeException extends Exception {
		public InvalidTypeException(String type) {
			super("Unknown Item Type: " + type);
		}
	}

}

