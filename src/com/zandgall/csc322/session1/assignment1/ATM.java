/* CSC322 SESSION 1: ASSIGNMENT 1 - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ATM.java - ATM
 # Lets the user interact with 10 account instances using terminal input

 : MADE IN NEOVIM */

package com.zandgall.csc322.session1.assignment1;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class ATM {
  public static void main(String[] args) {
    // Construct 10 accounts with $100 balance, and 0.65 interest rate
    ArrayList<Account> accounts = new ArrayList<Account>();
    for(int i = 0; i < 10; i++)
      accounts.add(new Account(i, 100, 0.65));

    // Scanner to be used for all user input
    Scanner input = new Scanner(System.in);
    while(true) { // Forever prompt for an ID and then run ATM on it
      System.out.print("Enter the account id: ");
      int id = -1; 
      
      try {
        id = input.nextInt();
      } catch(InputMismatchException e) {
        input.nextLine(); // Flush invalid input
        // User didn't enter an integer, feedback printed in following while loop
        // id is still -1
      }

      // Verify that a valid ID is retrieved
      while(id < 0 || id >= 10) {
        System.out.print("Please enter an integer id between 0 and 9 (inclusive): ");
        try {
          id = input.nextInt();
        } catch(InputMismatchException e) {
          input.nextLine(); // Flush invalid input
          // User didn't enter an integer, feedback printed next loop
        }
      }

      // Access the account, and start the menu loop
      Account account = accounts.get(id);
      boolean menu = true;
      while(menu) {
        // Print main menu
        System.out.println();
        System.out.println("Main Menu");
        System.out.println();
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Account Information");
        System.out.println("5. Exit (choose different account)");
        System.out.println();
        // ~~~~~~~~~~~~~~~

        // Ask user for selection, grab it, and add an extra line for padding
        System.out.print("Please make a selection: ");
        int selection = input.nextInt();
        System.out.println();
        
        // Switch on the menu selection
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
            menu = false; // Quit menu loop
            break;
          default:
            System.out.println("Invalid menu choice!");
        }
      }
    }
  }
};
