/* CSC322 SESSION 6: EXERCISE - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Main
 # Run an example of GenericStack

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.exercise;

public class Main {
	public static void main(String[] args) {
		GenericStack<Integer> stack = new GenericStack<Integer>();
		System.out.printf("Creating stack! Size: %d, Capacity: %d%n", stack.size(), stack.capacity());
		while(stack.size() <= 64) {
			stack.push(stack.size());
			System.out.printf("\tPushed %d onto the stack! (Size: %d, Capacity: %d)%n", stack.peek(), stack.size(), stack.capacity());
		}
		System.out.println();

		System.out.println("Emptying stack:");
		while(!stack.isEmpty())
			System.out.printf("\tPopped %d off the stack! (Size: %d, Capacity: %d)%n", stack.pop(), stack.size(), stack.capacity());
		System.out.println("Note: Notice that when popping, size is never equal to capacity - 1, meaning that 'push() pop() push() pop() ...' would never result in a chain of reallocation");
		System.out.println();

		System.out.printf("Stack is empty? %b%n", stack.isEmpty());

		System.out.println();
		try {
			stack.pop();
		} catch (Exception e) {
			System.out.println("This happens when you try to pop an empty stack:");
			e.printStackTrace();
		}

		System.out.println();
		try {
			stack.peek();
		} catch (Exception e) {
			System.out.println("This happens when you try to peek an empty stack:");
			e.printStackTrace();
		}
	}
}

