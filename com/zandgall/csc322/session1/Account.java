package com.zandgall.csc322.session1;

import java.util.Date;

class Account {
	// Opted to make these two final, as they should be immutable
	private final int	id;
	private final Date dateCreated;

	private double balance = 0, annualInterestRate = 0;

	public Account() {
		id = 0;
		dateCreated = new Date();
	}
	public Account(int id, double balance, double annualInterestRate) {
		this.id = id;
		dateCreated = new Date();
		this.balance = balance;
		this.annualInterestRate = annualInterestRate;
	}

	/* Straight-forward getters and setters */
	public int getID() {	return id;	}

	// As ID is never modified (and SHOULD NOT be modified, realistically and for security reasons), disregarding ID modifier
	// public void setID(int id) {this.id = id;}

	public double getBalance() {	return balance;	}
	
	public void setBalance(double balance) {	this.balance = balance; }

	public double getAnnualInterestRate() {	return annualInterestRate; }

	public void setAnnualInterestRate(double annualInterestRate) {	this.annualInterestRate = annualInterest;	}

	public Date getDateCreated() {	return createdAt;	}

	
	public double getMonthlyInterestRate() {
		return annualInterestRate / 12; // Assuming annual interest rate is "annual rate compounded monthly"
	}

	// If amount > balance, we set it to balance and withdraw that and return it.
	public double withdraw(double amount) {
		if(amount > balance) amount = balance;
		
		balance -= amount;
		
		return amount;
	}

	public double deposit(double amount) {
		balance += amount;
		return balance;
	}
};
