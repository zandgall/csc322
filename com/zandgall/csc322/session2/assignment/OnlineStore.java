/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## OnlineStore
 # An interactive terminal application that lets you explore an inventory

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class OnlineStore {
	
	private ItemInventory inventory;

	public OnlineStore() throws IOException, ItemInventory.InvalidTypeException {
		inventory = ItemInventory.load("inventory.txt");
	}

	public void start() {
		Scanner s = new Scanner(System.in);

		while(true) {
			System.out.println("Welcome to eMart");
			System.out.println(" 1) Show all items");
			System.out.println(" 2) Show only Music CDs");
			System.out.println(" 3) Show only Books");
			System.out.println(" 4) Show only Software");
			System.out.println(" 5) Exit");

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

	public void showItems() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.printf("%-30s %10s %10s %10s%n", "Title", "Type", "Price", "Quantity");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		for(int i = 0; i < inventory.size(); i++) {
			ItemEntry entry = inventory.getEntry(i);
			Item item = entry.getItem();
			System.out.printf("%-30s %10s %10.2f %10d%n", item.getTitle(), item.getType(), item.getPrice(), entry.getQuantity());
		}

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void showMusicCDs() {

	}

	public void showBooks() {

	}

	public void showSoftware() {

	}
}
