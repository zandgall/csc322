/* CSC322 SESSION 2: EXERCISE - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Shuffle.java - Shuffle
 # Provides one function to shuffle a given arraylist, along with a main method to demonstrate the shuffling

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.exercise;

import java.util.ArrayList;
import java.util.Random;

public class Shuffle {

	public static void shuffle(ArrayList<Integer> list) {
		// Create Random instance
		Random r = new Random();
		
		// Loop through every index in the list
		for(int i = 0; i < list.size(); i++) {
			// Pick a random index in the list,
			int swapWithIndex = r.nextInt(list.size());

			// And swap list[i] with list[swapWithIndex]
			Integer temporary = list.get(i);
			list.set(i, list.get(swapWithIndex));
			list.set(swapWithIndex, temporary);
		}
	}

	public static void main(String[] args) {
		// Create a list and add numbers 0 through 9
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
			list.add(i);

		// Print out the current list
		System.out.print("List: ");
		for(Integer a : list) {
			System.out.print(a + ", ");
		}
		System.out.println();
		System.out.println(); // Line Spacing

		// Shuffle a few times with printing the output
		for(int i = 1; i <= 5; i++) {
			// Shuffle the list
			shuffle(list);

			// Print out shuffled list
			System.out.printf("After shuffle #%d: ", i);

			for(Integer a : list) {
				System.out.print(a + ", ");
			}
			System.out.println();
		}
	}

}
