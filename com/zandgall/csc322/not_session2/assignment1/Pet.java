package com.zandgall.csc322.session2.assignment1;

class InvalidAgeException extends Exception {
	public InvalidAgeException(int age) {
		super("Invalid Age! Only ages 1-50 allowed, but " + age + " provided!");
	}
}

public class Pet {
	private String name;
	private int age;
	public Pet(String name, int age) throws InvalidAgeException {
		if(age < 1 || age > 50)
			throw new InvalidAgeException(age);
		this.name = name;
		this.age = age;
	}
	public int getAge() throws InvalidAgeException {
		if(age < 1 || age > 50)
			throw new InvalidAgeException(age);
		return age;
	}
	public String getName() {return name;}
	public void setAge(int age) {this.age = age;}
	public void setName(String name) {this.name = name;}
}
