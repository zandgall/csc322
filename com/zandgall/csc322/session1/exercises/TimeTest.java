package com.zandgall.csc322.session1.exercises;

public class TimeTest {
	public static void main(String[] args) {
		// Construct times for now, the epoch+555550000, and 5:23:55
		Time a, b, c;
		a = new Time(); // Whatever the time is at UTC+0/GMT (which is currently 5 hours ahead of CST)
		b = new Time(555550000); // Should produce 10:19:10
		c = new Time(5, 23, 55); // 5:23:55
		
		// Print output
		System.out.println("A: " + a.getHour() + ":" + a.getMinute() + ":" + a.getSecond());
		System.out.println("B: " + b.getHour() + ":" + b.getMinute() + ":" + b.getSecond());
		System.out.println("C: " + c.getHour() + ":" + c.getMinute() + ":" + c.getSecond());
	}
};
