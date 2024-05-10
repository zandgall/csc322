# CSC322
Class projects for CSC322 - Object Oriented Programming in Java

## Session 1

### Assignment 1

A bank-like `Account` class with data points for a balance, interest rate, date created, and an ID. Getters and setters for some, with a few special methods to modify or retrieve pieces of information.
Along with an `ATM` class with a main method that allows a user to interact with 10 different Account instances using the terminal.

Compile and Run: 
```sh
cd csc322
javac -d build com/zandgall/csc322/session1/assignment1/ATM.java
java -cp build com.zandgall.csc322.session1.assignment1.ATM
```

### Exercises

A `Time` class that stores an hour, minute, and second as integers. It has methods for getting and setting all three, along with a method that sets them automatically based on a Unix timestamp.
The `TimeTest` class has a main method that initializes three Time instances, and prints their resulting hour, minute, and second.
Also includes a chart that shows how the Time class is designed.

Compile and Run: 
```sh
cd csc322
javac -d build com/zandgall/csc322/session1/exercises/TimeTest.java
java -cp build com.zandgall.csc322.session1.exercises.TimeTest
```
