/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ItemInventory
 #

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.io.IOException;
import java.io.File;

public class ItemInventory {

	protected ArrayList<ItemEntry> entries = new ArrayList<ItemEntry>();

	public ItemInventory() {}

	public static ItemInventory load(String filename) throws IOException, InvalidTypeException {
		File file = new File(filename);
		Scanner s = new Scanner(file);

		ItemInventory inventory = new ItemInventory();

		String line;
		while(s.hasNextLine()) {
			line = s.nextLine();
			Item item;

			String attribs[] = line.split("|");

			switch(attribs[0]) {
				case "music":
					item = new MusicCD(attribs[1], attribs[2], new Date(attribs[3]), attribs[4], attribs[5], Integer.parseInt(attribs[6]), attribs[7], Float.parseFloat(attribs[8]));
					break;
				case "book":
					item = new Book(attribs[1], attribs[2], attribs[3], attribs[4], Integer.parseInt(attribs[5]), Float.parseFloat(attribs[6]));
					break;
				case "software":
					item = new Software(attribs[1], attribs[2], Float.parseFloat(attribs[3]));
					break;
				default:
					throw new InvalidTypeException(attribs[0]);
			}

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

