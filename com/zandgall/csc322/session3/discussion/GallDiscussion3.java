package com.zandgall.csc322.session3.discussion;

import java.util.ArrayList;
import java.util.Scanner;

public class GallDiscussion3 {
	/* A basic class that defines two fields with getters, and one abstract method  */
	private static abstract class Person {
		private String name;
		private int age;

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public int getAge() { return age; }

		public String getName() { return name; }

		public abstract String dailyActivities();
	}

	/* A sub class that adds no fields, but implements the dailyActivities method */
	private static class Adult extends Person {
		public Adult(String name, int age) {
			super(name, age);
			if(age < 18)
				throw new RuntimeException("Cannot create adult with age " + age);	
		}
		public String dailyActivities() {
			return getName() + " goes to work";
		}
	}

	/* A sub class that adds a 'guardians' field, and implements the dailyActivities method differently than Adult */
	private static class Child extends Person {
		private Adult[] guardians;

		public Child(Adult[] guardians, String name, int age) {
			super(name, age);
			if(age >= 18)
				throw new RuntimeException("Cannot create child with age " + age);
			this.guardians = guardians;
		}

		public String dailyActivities() {
			if(getAge() < 5) /* makes a call to a parent class */
				return getName() + " takes a nap";
			else
				return getName() + " goes to school";
		}
	}

	public static void main(String[] args) {
		// 1: One of the uses of abstract classes, is to have aggregates 
		//    of one type that have different method definitions
		ArrayList<Person> people = new ArrayList<Person>();


		// 2: Despite being different classes, we can store both Child 
		//    and Adult in a list of "Person"
		Adult greg = new Adult("Greg", 34);
		Adult bobby = new Adult("Bobby", 36);
		Adult sarah = new Adult("Sarah", 39);
		Child johnny = new Child(new Adult[] {greg, bobby, sarah}, "Johnny", 12);
		
		people.add(greg);
		people.add(bobby);
		people.add(sarah);
		people.add(johnny);


		// 3: We can similarly dynamically choose a type to generate and store, 
		//    as long as it's a subtype of Person
		//    (No input check!)
		System.out.println("Create new person!");
		System.out.print("Name: ");
		Scanner s = new Scanner(System.in);
		String name = s.next();
		System.out.print("Age: ");
		int age = s.nextInt();

		if(age < 18)
			people.add(new Child(new Adult[]{}, name, age));
		else
			people.add(new Adult(name, age));

		// 4: The Person class describes a 'dailyActivities' method, which can be 
		//    defined differently for different subclasses. Because we are only 
		//    thinking in terms of Person, we can call this function to do different 
		//    things depending on the subclass
		for(Person p : people) {
			System.out.printf("%s is %d years old%n", p.getName(), p.getAge());
			System.out.println(p.dailyActivities());
			System.out.println();
		}
	}
}
