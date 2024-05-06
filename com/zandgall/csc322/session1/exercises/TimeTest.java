package com.zandgall.csc322.session1.exercises;

public class TimeTest {
	public static void main(String[] args) {
		Time a, b, c;
		a = new Time();
		b = new Time(555550000);
		c = new Time(5, 23, 55);

		System.out.println("A: " + a.getHour() + ":" + a.getMinute() + ":" + a.getSecond());
		System.out.println("B: " + b.getHour() + ":" + b.getMinute() + ":" + b.getSecond());
		System.out.println("C: " + c.getHour() + ":" + c.getMinute() + ":" + c.getSecond());
	}
};
