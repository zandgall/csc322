/* CSC322 SESSION 6: ASSIGNMENT - PROF. SUSAN FURTNEY
 > I certify, that this computer program submitted by me is all of my own work.
 > ZANDER GALL - GALLA@CSP.EDU

 ## Program
 # A "driver program" that lets the user interact with the designed bags

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.assignment;

import java.util.Scanner;

public class Program4 {
	private IBag<String> bag;

	public Program4() {}

	public void run() {
		// Get what bag type to use, and generater one
		System.out.print("What type of bag do you want? ");
		String answer = "";
		Scanner s = new Scanner(System.in);
		while(!answer.equals("unsorted") && !answer.equals("sorted")) {
			System.out.print("sorted/unsorted: ");
			answer = s.next();
		}

		// The only mention of UnsortedBag and SortedBag class, everything else is just "IBag" methods
		if(answer.equals("unsorted"))
			bag = new UnsortedBag<String>();
		else
			bag = new SortedBag<String>();

		System.out.print("How many items do you want in the bag? ");
		int capacity = -1;
		while(capacity < 0) {
			try {
				capacity = s.nextInt();
			} catch (Exception e) {
				capacity = -1;
			}
		}

		// Clear any input
		s.nextLine();

		for(int i = 1; i <= capacity; i++) {
			System.out.printf("Enter item %d: ", i);
			bag.add(s.nextLine());
		}

		System.out.println();
		System.out.println("You can check if something is in the bag. (Type done to stop)");

		do {
			answer = s.nextLine();
			if(!answer.equals("done")) {
				if(bag.contains(answer))
					System.out.println("Yes, it's in the bag!");
				else
					System.out.println("No, it's not in the bag!");
			}
		} while(!answer.equals("done"));


		System.out.println();
		System.out.println("Let's empty the bag");
		while(!bag.empty())
			System.out.printf("Took \"%s\" out of the bag%n", bag.remove());
	}

	public static void main(String[] args) {
		new Program4().run();
	}
}

