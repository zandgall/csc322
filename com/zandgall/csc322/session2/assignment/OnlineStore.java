/* CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## OnlineStore
 # An interactive terminal application that lets you explore an inventory

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class OnlineStore {

	/**
	 * The 'Date' class has several deprecated function, including the string constructor and '.toString()' function
 	 * This is the currently accepted alternative/replacement, which throws 'ParseException' on parse
	 */
	public static final DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

	private ItemInventory inventory;

	/**
	 * Load an inventory, passing all errors to the {@link Main#main(String[])} function
 	 */
	public OnlineStore() throws IOException, ItemInventory.InvalidTypeException, ParseException {
		inventory = ItemInventory.load("inventory.txt");
	}

	/**
	 * Run a terminal menu that calls other functions to display information the user queries
	 */
	public void start() {
		Scanner s = new Scanner(System.in);

		while(true) {
			System.out.println("Welcome to eMart");
			System.out.println(" 1) Show all items");
			System.out.println(" 2) Show only Music CDs");
			System.out.println(" 3) Show only Books");
			System.out.println(" 4) Show only Software");
			System.out.println(" 5) Exit");
			System.out.print("> ");
			int selection = s.nextInt();
			switch(selection) {
			case 1:
				showItems();
				break;
			case 2:
				showMusicCDs();
				break;
			case 3:
				showBooks();
				break;
			case 4:
				showSoftware();
				break;
			case 5:
				return;
			default:
				System.err.println("Invalid selection!");
			}
		}
	}

	/**
	 * Print a table generic information about all ItemEtries in 'inventory'
	 */
	public void showItems() {
		System.out.println("╔══════════════════════════════╦══════════╦══════════╦══════════╗");
		System.out.printf("║%-30s║%10s║%10s║%10s║%n", "Title", "Type", "Price", "Quantity");
		System.out.println("╚══════════════════════════════╩══════════╩══════════╩══════════╝");
		System.out.println("┌──────────────────────────────┬──────────┬──────────┬──────────┐");

		for(int i = 0; i < inventory.size(); i++) {
			ItemEntry entry = inventory.getEntry(i);
			Item item = entry.getItem();
			System.out.printf("│%-30s│%10s│%10.2f│%10d│%n", item.getTitle(), item.getType(), item.getPrice(), entry.getQuantity());
		}

		System.out.println("└──────────────────────────────┴──────────┴──────────┴──────────┘");
	}

	/**
	 * Print a table with music cd specific information, of all ItemEntries in 'inventory' that are instances of MusicCD.
	 */
	public void showMusicCDs() {
		System.out.println("╔════════════════════╦════════════════════╦══════════╦════════════════════════════╦══════════════════════╦══════════╦══════════╦══════════╦══════════╗");
		System.out.printf("║%-20s║%-20s║%-10s║%28s║%22s║%10s║%10s║%10s║%10s║%n", "Title", "Artists", "Release", "Label", "Record", "Length", "Genres", "Price", "Quantity");
		System.out.println("╚════════════════════╩════════════════════╩══════════╩════════════════════════════╩══════════════════════╩══════════╩══════════╦══════════╩══════════╝");
		System.out.println("┌────────────────────┬────────────────────┬──────────┬────────────────────────────┬──────────────────────┬──────────┬──────────┬──────────┬──────────┐");

		for(int i = 0; i < inventory.size(); i++) {
			ItemEntry entry = inventory.getEntry(i);
			if(entry.getItem() instanceof MusicCD musicCD) {
				String dateString = dateFormatter.format(musicCD.getReleaseDate());
				System.out.printf("│%-20s│%-20s│%-10s│%28s│%22s│%10d│%10s│%10.2f│%10d│%n", musicCD.getTitle(), musicCD.getArtists(), dateString, musicCD.getLabel(), musicCD.getRecordCompany(), musicCD.getTotalLength(), musicCD.getGenres(), musicCD.getPrice(), entry.getQuantity());
			}
		}

		System.out.println("└────────────────────┴────────────────────┴──────────┴────────────────────────────┴──────────────────────┴──────────┴──────────┴──────────┴──────────┘");
	}

	/**
	 * Print a table with book specific information, of all ItemEntries in 'inventory' that are instances of Book.
	 */
	public void showBooks() {
		System.out.println("╔══════════════════════════════╦════════════════════╦════════╦════════════════════╦════╦══════════╦══════════╗");
		System.out.printf("║%-30s║%-20s║%8s║%20s║%4s║%10s║%10s║%n", "Title", "Authors", "Edition", "Publisher", "Year", "Price", "Quantity");
		System.out.println("╚══════════════════════════════╩════════════════════╩════════╩════════════════════╩════╩══════════╩══════════╝");
		System.out.println("┌──────────────────────────────┬────────────────────┬────────┬────────────────────┬────┬──────────┬──────────┐");

		for(int i = 0; i < inventory.size(); i++) {
			ItemEntry entry = inventory.getEntry(i);
			if(entry.getItem() instanceof Book book) {
				System.out.printf("│%-30s│%-20s│%8s│%20s│%4d│%10.2f│%10d│%n", book.getTitle(), book.getAuthors(), book.getEdition(), book.getPublisher(), book.getPubYear(), book.getPrice(), entry.getQuantity());
			}
		}
		
		System.out.println("└──────────────────────────────┴────────────────────┴────────┴────────────────────┴────┴──────────┴──────────┘");

	}

	/**
	 * Print a table with software specific information, of all ItemEntries in 'inventory' that are instances of Software.
	 */
	public void showSoftware() {
		System.out.println("╔══════════════════════════════╦════════╦══════════╦══════════╗");
		System.out.printf("║%-30s║%8s║%10s║%10s║%n", "Title", "Version", "Price", "Quantity");
		System.out.println("╚══════════════════════════════╩════════╩══════════╩══════════╝");
		System.out.println("┌──────────────────────────────┬────────┬──────────┬──────────┐");

		for(int i = 0; i < inventory.size(); i++) {
			ItemEntry entry = inventory.getEntry(i);
			if(entry.getItem() instanceof Software software) {
				System.out.printf("│%-30s│%8s│%10.2f│%10d│%n", software.getTitle(), software.getVersion(), software.getPrice(), entry.getQuantity());
			}
		}

		System.out.println("└──────────────────────────────┴────────┴──────────┴──────────┘");
	}
}
