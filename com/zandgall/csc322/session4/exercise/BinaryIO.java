/* CSC322 SESSION 4: EXERCISE - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## BinaryIO
 # Write and read objects from a file

 : MADE IN NEOVIM */

package com.zandgall.csc322.session4.exercise;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Date;

public class BinaryIO {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Exercise17_05.dat"))) {
			out.writeObject(new int[] {1, 2, 3, 4, 5});
			out.writeObject(new Date());
			out.writeObject(5.5);
		}

		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("Exercise17_05.dat"))) {
			int[]  numbers     =  (int[]) in.readObject();
			Date   timeWritten =   (Date) in.readObject();
			double value       = (double) in.readObject();
			// Ignore use of deprecated Date.toString
			System.out.println("At " + timeWritten);
			System.out.print("Numbers: ");
			for(int i : numbers)
				System.out.print(i + ", ");
			System.out.println("were written, along with " + value);
		}
	}
}

