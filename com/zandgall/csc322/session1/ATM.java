package com.zandgall.csc322.session1;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class ATM {
  public static void main(String[] args) {
    ArrayList<Account> accounts = new ArrayList<Account>();
    for(int i = 0; i < 10; i++)
      accounts.add(new Account(i, 100, 0.65));

    Scanner input = new Scanner(System.in);
    while(true) { // Forever prompt for an ID and then run ATM on it
      System.out.print("Enter the account id: ");
      int id = input.nextInt();
      while(id < 0 || id >= 10) {
        System.out.print("Please enter an id between 0 and 9 (inclusive): ");
        id = input.nextInt();
      }

      Account account = accounts.get(id);
      boolean menu = true;
      while(menu) { // Forever prompt for an action on this account, if 5 then we will use break to exit
        System.out.println();
        System.out.println("Main Menu");
        System.out.println();
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Account Information");
        System.out.println("5. Exit (choose different account)");
        System.out.println();
        System.out.print("Please make a selection: ");
        int selection = input.nextInt();
        System.out.println();
        switch(selection) {
          case 1:
            System.out.print("The current balance is: ");
            System.out.println(account.getBalance());
            break;
          case 2:
            System.out.print("Please enter the amount to withdraw: ");
            System.out.println("Amount withdrawn is: " + account.withdraw(input.nextDouble()));
            break;
          case 3:
            System.out.print("Please enter the amount to deposit: ");
            System.out.println("Amount after deposit is: " + account.deposit(input.nextDouble()));
            break;
          case 4:
            System.out.print("Account was created on: ");
            System.out.println(account.getDateCreated());
            System.out.print("Account interest rate: ");
            System.out.println(account.getAnnualInterestRate());
            System.out.print("Account balance: ");
            System.out.println(account.getBalance());
            break;
          case 5:
            menu = false;
            break;
          default:
            System.out.println("Invalid menu choice!");
        }
      }
    }
  }
};
