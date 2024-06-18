# CSC322
Class projects for CSC322 - Object Oriented Programming in Java

## Session 6

### Assignment

An interface called "IBag" that just declares methods for adding an item, taking out an item, and checking if a bag contains an item. Two implementations, one "UnsortedBag" that is just a functional wrapper for ArrayList, another "SortedBag" that sorts the list every time you add an item, and uses the binary search algorithm to check whether an item exists in the bag.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session6/assignment/Program4.java
java -cp ../build com.zandgall.csc322.session6.assignment.Program4
```

### Exercise

Generic stack implementation. Using generics to construct a stack that reallocates as the stack outgrows its capacity or shrinks too little.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session6/exercise/Main.java
java -cp ../build com.zandgall.csc322.session6.exercise.Main
```

### Discussion

A demonstration of recursion. Create a tree where each branch has it's own tree with n-1 and m-2 branches depending on the direction (left vs right).
Uses [JavaFX](#javafx)

```sh
cd csc322/src
javac -p $JAVAFX_PATH --add-modules javafx.controls -d ../build com/zandgall/csc322/session6/discussion/Recursion.java
java -p $JAVAFX_PATH --add-modules javafx.controls -cp ../build Recursion

# or

cd csc322/src
javafxc -d ../build com/zandgall/csc322/session6/discussion/Recursion.java
javafx -cp ../build Recursion
```

## Session 5

### Assignment

A simple 'AddressBook' GUI that loads and saves a data.bin file to keep track of people with information. This assignment utilizes Java Object I/O Streams, with a custom Serializable class. This project also uses JavaFX. See [this section](#javafx) to set up JavaFX on command-line.

Compile and Run: (Works with JavaFX 21 SDK)
```sh
cd csc322/src
javac -p $JAVAFX_PATH --add-modules javafx.controls -d ../build com/zandgall/csc322/session5/assignment/AddressBook.java
java -p $JAVAFX_PATH --add-modules javafx.controls -cp ../build com.zandgall.csc322.session5.assignment.AddressBook

# or

cd csc322/src
javafxc -d ../build com/zandgall/csc322/session5/assignment/AddressBook.java
javafx -cp ../build com.zandgall.csc322.session5.assignment.AddressBook
```

### Exercise

A simple JavaFX program that gives you a UI to type in two numbers, and add, subtract, multiply, or divide them. [How to set up JavaFX](#javafx)

Compile and Run: (Works with JavaFX 21 SDK)
```sh
cd csc322/src
javac -p $JAVAFX_PATH --add-modules javafx.controls -d ../build com/zandgall/csc322/session5/exercise/Calculator.java
java -p $JAVAFX_PATH --add-modules javafx.controls -cp ../build com.zandgall.csc322.session5.exercise.Calculator

# or

cd csc322/src
javafxc -d ../build com/zandgall/csc322/session5/exercise/Calculator.java
javafx -cp ../build com.zandgall.csc322.session5.exercise.Calculator
```

## Session 4

### Assignment (cont. from Session 3)

An implementation of a provided set of interfaces. A larger project that provides CLI to make and take tests.
![Interface and relationship diagram](assets/interface_diagram.png)

You first use the TestMaker program to make a test, then you use the TestTaker program to take a test.
```sh
cd csc322/src
javac -d ../build com/jsoftware/test/TestMaker.java
java -cp ../build com.jsoftware.test.TestMaker

javac -d ../build com/jsoftware/test/TestTaker.java
java -cp ../build com.jsoftware.test.TestTaker
```

### Exercise

A simple demonstration of Object I/O Streams

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session4/exercise/BinaryIO.java
java -cp ../build com.zandgall.csc322.session4.exercise.BinaryIO
```

### Discussion

A humorous maze generator, made without consulting any outside resources like documentation or other projects or even IDE suggestions. With the only exception being a place to copy and paste the characters used to display the maze.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session4/discussion/GallDiscussion4.java
java -cp ../build com.zandgall.csc322.session4.discussion.GallDiscussion4
```

## Session 3

### Exercise

A simple project that prints the vertex form of a parabola entered by the user. It makes use of a custom "Rational" class that extends Number, it has methods to add, subtract, divide, and multiply with other numbers.
(If this were a C++ assignment, I could even implement -+/* as operators ;) )

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session3/exercise/Parabola.java
java -cp ../build com.zandgall.csc322.session3.exercise.Parabola
```

### Discussion

An example that demonstrates ways to use abstract classes. Including having a list of different child classes, holding common fields and methods, and calling common methods without having to know what the child class is.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session3/discussion/GallDiscussion3.java
java -cp ../build com.zandgall.csc322.session3.discussion.GallDiscussion3
```

## Session 2

### Assignment

A project that loads a text file to parse an Inventory, and provides a command line interface to list different queries of items.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session2/assignment/Main.java
java -cp ../build com.zandgall.csc322.session2.assignment.Main
```
(Make sure that inventory.txt is in your working directory)

### Discsussion

A small class that overrides the `Object.equals()` function, with a demonstration of it. Used in a class dicussion.

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session2/discussion/Comparison.java
java -ea -cp ../build com.zandgall.csc322.session2.exercise.Comparison
```

### Exercise

A simple 'shuffle' function and demonstration. This little program creates an ArrayList and shuffles it several times. The shuffling is performed by swapping every entry in the list with a randomly picked entry elsewhere in the list. Like so:

```pseudo
for (i from 0 to size of list)
    swap list[i] with list[random from 0 to size of list]
```

Compile and Run:
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session2/exercise/Shuffle.java
java -cp ../build com.zandgall.csc322.session2.exercise.Shuffle
```

## Session 1

### Assignment 1

A bank-like `Account` class with data points for a balance, interest rate, date created, and an ID. Getters and setters for some, with a few special methods to modify or retrieve pieces of information.
Along with an `ATM` class with a main method that allows a user to interact with 10 different Account instances using the terminal.

Compile and Run: 
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session1/assignment1/ATM.java
java -cp ../build com.zandgall.csc322.session1.assignment1.ATM
```

### Exercises

A `Time` class that stores an hour, minute, and second as integers. It has methods for getting and setting all three, along with a method that sets them automatically based on a Unix timestamp.
The `TimeTest` class has a main method that initializes three Time instances, and prints their resulting hour, minute, and second.
Also includes a chart that shows how the Time class is designed.

Compile and Run: 
```sh
cd csc322/src
javac -d ../build com/zandgall/csc322/session1/exercises/TimeTest.java
java -cp ../build com.zandgall.csc322.session1.exercises.TimeTest
```


# JavaFX

Here I'll explain two ways to set up JavaFX

## Method 1: Download and link as module

This is the way that [openjfx](https://openjfx.io/openjfx-docs/#install-javafx) teaches you how to install.

The first thing you do, is download a [JavaFX SDK](https://gluonhq.com/products/javafx/). The second thing you'll want to do, is add the path to JavaFX as a path variable.

This can be done on Windows with: `set JAVAFX_PATH="path\to\javafx-sdk\lib"`
And on Linux/Mac with: `export JAVAFX_PATH="path/to/javafx-sdk/lib"`

(System environment changes can be made permanent on Linux by modifying your ~/.bashrc or ~/.zshrc depending on the shell you use)

## Method 2: Use a standalone Runtime Environment

Some people repackage Java to include JavaFX, so you wouldn't need to specify and add modules every time you compile and run.

You can download one of these packages from [Azul](https://www.azul.com/downloads/?package=jdk-fx#zulu). Once downloaded, you'll see a `java` and `javac` executable file that operates the exact same way as your normal java/javac.

I recommend *making a shortcut/symlink* of these two files, with 'fx' in the name. I use 'javafx' and 'javafxc'. You can place these shortcuts/symlinks either somewhere that is already included in the system Path, for example your current Java's installation bin directory, or add it's location to the path yourself.
