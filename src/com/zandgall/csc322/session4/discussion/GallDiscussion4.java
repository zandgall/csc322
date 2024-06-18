/* Made in NEOVIM with no outside sources 
 * except https://en.wikipedia.org/wiki/Box-drawing_characters to copy-paste the special characters */

package com.zandgall.csc322.session4.discussion;

import java.util.Scanner;
import java.util.Random;

public class GallDiscussion4 {
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("We're gonna make you a maze!");
		Thread.sleep(2000); // Pause 2 seconds
		
		// Handle important real legal stuff 
		System.out.println("By using this software you acknowledge that this maze may not have a real solution, and you might be trapped forever");
		Thread.sleep(5000); // 5 seconds
		System.out.println("Please sign this very real, very serious, and very legally binding document to agree to not sue us!");
		Thread.sleep(5000); // 5 seconds
		System.out.println("(Not like you could sue us from your maze)");
		Thread.sleep(2000); // 2 seconds
		System.out.print("Signature: ");
		Scanner s = new Scanner(System.in);
		String importantLegalSignature = s.nextLine();

		// Gather information for generation
		System.out.println("How many characters wide do you want your maze to be?");
		System.out.print(" > ");
		int width = s.nextInt();
		System.out.println("How many characters tall do you want your maze to be?");
		System.out.print(" > ");
		int height = s.nextInt();
		Random rand = new Random();

		// List of characters to sample
		char[] walls = new char[] {'├', '│', '┤', '┬', '─', '┴', '┌', '┐', '└', '┘', '┼'};

		// Generate and print maze!
		for(int y = 0; y < height; y++) {
			// Print left wall, top corner, bottom corner, or opening
			if(y == 0)
				System.out.print('┌');
			else if(y == height - 2)
				System.out.print(' ');
			else if(y == height - 1)
				System.out.print('└');
			else
				System.out.print(walls[rand.nextInt(2)]); // ├ or │

			// Print from left to right
			for(int x = 1; x < width-1; x++) {
				// If top or bottom, print top or bottom border
				if(y == 0)
					System.out.print(walls[rand.nextInt(2)+3]); // ┬ or ─
				else if(y==height-1)
					System.out.print(walls[rand.nextInt(2)+4]); // ─ or ┴
				// otherwise print anything in sample list
				else
					System.out.print(walls[rand.nextInt(walls.length)]);
			}
			// Print right wall, top corner, bottom corner, or opening
			if(y == 0)
				System.out.print('┐');
			else if(y == 1)
				System.out.print(' ');
			else if(y == height - 1)
				System.out.print('┘');
			else
				System.out.print(walls[rand.nextInt(2) + 1]); // │ or ┤

			System.out.println();
		}
		

	}
}
