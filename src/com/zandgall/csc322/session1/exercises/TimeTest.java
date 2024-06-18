/* CSC322 SESSION 1: BOOK EXERCISES - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## TimeTest.java - TimeTest
 # Creates 3 'Time' instances and prints their contents.

 : MADE IN NEOVIM */

package com.zandgall.csc322.session1.exercises;

public class TimeTest {
	public static void main(String[] args) {
		// Construct times for now, the epoch+555550000, and 5:23:55
		Time a, b, c;
		a = new Time(); // Whatever the time is at UTC+0/GMT (which is currently 5 hours ahead of CST)
		b = new Time(555550000); // Should produce 10:19:10
		c = new Time(5, 23, 55); // 5:23:55
		
		// Print output
		System.out.printf("A: %d:%02d:%02d%n", a.getHour(), a.getMinute(), a.getSecond());
		System.out.printf("B: %d:%02d:%02d%n", b.getHour(), b.getMinute(), b.getSecond());
		System.out.printf("C: %d:%02d:%02d%n", c.getHour(), c.getMinute(), c.getSecond());
	}
};
