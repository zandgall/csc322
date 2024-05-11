/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ItemInventory
 #

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

	public static ItemInventory load(String filename) throws IOException, InvalidTypeException, ParseException {
		File file = new File(filename);
		Scanner s = new Scanner(file);

		ItemInventory inventory = new ItemInventory();

		String line;
		while(s.hasNextLine()) {
			line = s.nextLine();
			Item item;

			String attribs[] = line.split("\\|");

			switch(attribs[0]) {
				case "music":
					item = new MusicCD(
						attribs[2], // Artists
						OnlineStore.dateFormatter.parse(attribs[3]), // Release Date - throws parse exception
						attribs[4], // Label
						attribs[5], // Record Company
						Integer.parseInt(attribs[6]), // Total Length
						attribs[7]); // Genres
					break;
				case "book":
					item = new Book(
						attribs[2], // authors
						attribs[3], // publisher
						attribs[4], // edition
						Integer.parseInt(attribs[5])); // Published Year
					break;
				case "software":
					item = new Software(attribs[2]); // Version
					break;
				default:
					throw new InvalidTypeException(attribs[0]);
			}

			item.setTitle(attribs[1]);
			item.setPrice(Float.parseFloat(attribs[attribs.length-2]));

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

