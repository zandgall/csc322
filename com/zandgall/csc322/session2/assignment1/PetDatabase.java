package com.zandgall.csc322.session2.assignment1;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

class FullDatabaseException extends Exception {
	public FullDatabaseException(int capacity) {
		super("Database Full! Reached capacity: " + capacity);
	}
}

class InvalidArgumentException extends Exception {
	public InvalidArgumentException(String argument) {
		super("Invalid Argument! \""+argument+"\"");
	}
}

class InvalidIdException extends Exception {
	public InvalidIdException(int id, int petCount) {
		super("Invalid ID! Only ID's 0-"+petCount+" allowed, but " + id + " provided!");
	}
}


public class PetDatabase {
	private static final Scanner s = new Scanner(System.in);
	private static final String filename = "pets.txt";
	public static int CAPACITY = 0;
	public static Pet[] pets = new Pet[100];

	public static void main(String[] args) {
		while(true) {
			int selection = getUserChoice();
			switch(selection) {
			case 1:
				showAllPets();
				break;
			case 2:
				addPets();
				break;
			case 3:
				try {
					removePet();
				} catch(InvalidIdException e) {
					System.out.println(e);
				}
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid selection");
			}
		}
	}

	private static void loadDatabase() {
		try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			try {
				while(reader.ready()) {
					String[] args = parseArgument(reader.readLine());
					addPet(args[0], Integer.parseInt(args[1]));
				}
			} catch(IOException|FullDatabaseException e) {
				e.printStackTrace();
			}
		} catch(FileNotFoundException e) {
			System.out.println("'pets.txt' does not exist! " + e);
		}
	}

	private static void saveDatabase() throws IOException {
		FileWriter writer = new FileWriter(filename);
		for(int i = 0; i < CAPACITY; i++)
			try {
				writer.write(pets[i].getName() + " " + pets[i].getAge() + System.lineSeparator());
			} catch(InvalidAgeException e) {
				System.err.println(e);
			}
	}

	private static int getUserChoice() {
		System.out.println("1) View all pets");
		System.out.println("2) Add new pets");
		System.out.println("3) Remove a pet");
		System.out.println("4) Exit program");
		System.out.print("Your choice > ");
		return s.nextInt();
	}

	private static void addPets() throws FullDatabaseException, InvalidAgeException {
		System.out.println("Type (name) (age) to add a pet. Type 'done' to stop.");
		while(true) {
			String name = s.next();
			if(name=="done")
				return;
			int age = s.nextInt();

			addPet(name, age);
		}
	}

	private static void addPet(String name, int age) throws FullDatabaseException {
		if(CAPACITY == pets.length)
			throw new FullDatabaseException(CAPACITY);
		pets[CAPACITY] = new Pet(name, age);
		CAPACITY++;
	}

	private static String[] parseArgument(String line) throws InvalidArgumentException {
		String[] out = line.split("\\s+"); // Split on any whitespace character(s)
		if(out.length!=2)
			throw new InvalidArgumentException(line);
	}

	private static void showAllPets() {
		printTableHeader();
		for(int i = 0; i < CAPACITY; i++)
			printTableRow(i, pets[i].getName(), pets[i].getAge());
		printTableFooter(CAPACITY);
	}

	private static void removePet() throws InvalidIdException {
		System.out.println("Enter an id to remove");
		int id = s.nextInt();
		if(id < 0 || id > CAPACITY)
			throw new InvalidIdException(id, CAPACITY);
		pets[id] = null;
		for(int i = id; i < CAPACITY-1; i++)
			pets[i] = pets[i+1];
		CAPACITY--;
	}

	private static void searchPetsByAge() {
		System.out.println("Enter an age to search for");
		int age = s.nextInt();
		printTableHeader();
		int printed = 0;
		for(int i = 0; i < CAPACITY; i++)
			if(pets[i].getAge()==age) {
				printTableRow(i, pets[i].getName(), pets[i].getAge());
				printed++;
			}
		printTableFooter(printed);
	}

	private static void printTableHeader() {
		System.out.println("┏━━━━┳━━━━━━━━━━━━┳━━━━━┓");
		System.out.println("┃ ID ┃ NAME       ┃ AGE ┃");
		System.out.println("┡━━━━╇━━━━━━━━━━━━╇━━━━━┩");
	}

	private static void printTableRow(int id, String name, int age) {
		System.out.printf("│ %2d │ %s10 │ %3d │", id, name, age);
	}

	private static void printTableFooter(int rowCount) {
		System.out.println("└────┴────────────┴─────┘");
		System.out.println(rowCount + " rows in set.");
	}
}
