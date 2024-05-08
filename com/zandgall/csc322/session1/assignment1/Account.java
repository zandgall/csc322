/* CSC322 SESSION 1: ASSIGNMENT 1 - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Account.java - Account
 # Stores an ID, date created, balance, and annual interest rate.
 # Can be modified using the methods provided, highlighting deposit and withdraw.
 
 : MADE IN NEOVIM */

package com.zandgall.csc322.session1.assignment1;

import java.util.Date;

class Account {
	// Opted to make these two final, as they should be immutable
	private final int  id;
	private final Date dateCreated;

	private double balance = 0, annualInterestRate = 0;

	public Account() {
		id = 0;
		dateCreated = new Date();
	}

	// Set id, balance, and interest rate based on given, and set dateCreated to now, (which is done automatically in default Date constructor)
	public Account(int id, double balance, double annualInterestRate) {
		this.id = id;
		dateCreated = new Date();
		this.balance = balance;
		this.annualInterestRate = annualInterestRate;
	}

	/* Straight-forward getters and setters */
	public double getBalance() { return balance; }

	public void setBalance(double balance) { this.balance = balance; }

	public double getAnnualInterestRate() { return annualInterestRate; }

	public void setAnnualInterestRate(double annualInterestRate) { this.annualInterestRate = annualInterestRate; }

	public Date getDateCreated() { return dateCreated; }

	public int getID() { return id; }

	// As ID is never modified (and SHOULD NOT be modified, realistically and for security reasons), disregarding ID modifier
	// public void setID(int id) {this.id = id;}


	/* Calculated property and methods */
	// Return monthly interest rate based on annual interest rate
	public double getMonthlyInterestRate() {
		return annualInterestRate / 12; // Accurate assuming annual interest rate is compounded monthly
	}

	// If amount > balance, we set 'amount' to balance. Withdraw amount, and return it.
	public double withdraw(double amount) {
		if(amount > balance) 
			amount = balance;
		balance -= amount;
		return amount;
	}

	// Add amount to balance and return new balance
	public double deposit(double amount) {
		balance += amount;
		return balance;
	}
};
