/* CSC322 SESSION 6: EXERCISE - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## GenericStack
 # A generic stack that utilizes an array to provide all the functionality of a Stack

 - You should see me do this in Assembly!

 : MADE IN NEOVIM */

package com.zandgall.csc322.session6.exercise;

public class GenericStack<E> {

	// Magic numbers
	private static final int MIN_CAPACITY = 4, MAX_REALLOCATION = 32;

	private E array[];
	private int size = 0;

	public int size() { return size; }
	public int capacity() { return array.length; } // Borrowing from C++ std::vector<T>
	
	// Start with size of 4 to test extending list size easily
	@SuppressWarnings("unchecked") // <- what super smart and cool people do when working with java generics
	public GenericStack() {
		// Not allowed to just allocate a new array of a generic, using new Object[], and ignoring warnings	
		this.array = (E[])new Object[MIN_CAPACITY];
	}

	public E peek() {
		if(isEmpty())
			throw new IndexOutOfBoundsException("Can't peek when stack is empty!");
		return array[size-1];
	}

	public void push(E o) {
		checkExtend();
		array[size] = o;
		size++;
	}

	public E pop() {
		if(isEmpty())
			throw new IndexOutOfBoundsException("Attempted to pop an empty stack!");

		size--;
		E out = array[size];
		// if we shrink, it might cause array[size] to be out of bounds, so get the result first, then check shrink, then return
		checkShrink();
		return out;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// Handle extending and shrinking
	@SuppressWarnings("unchecked")
	private void checkExtend() {
		// Only extend if the size hits the end
		if(size < array.length - 1)
			return;

		E old[] = array;

		// We double the capacity typically, but only ever increase by 32 at most
		int capacityIncrease = array.length;
		if(capacityIncrease > MAX_REALLOCATION)
			capacityIncrease = MAX_REALLOCATION;

		// Allocate new list and copy content	
		array = (E[])new Object[array.length + capacityIncrease];
		for(int i = 0; i < size; i++)
			array[i] = old[i];

		// Garbage collector will take care of old array
	}

	@SuppressWarnings("unchecked")
	private void checkShrink() {
		// We shrink if size < capacity - 32*2 (max_reallocation), or size < capacity / 4 and capacity > 4 (min_capacity)

		// Designed to never shrink so that 'size' is on the bounds of capacity
		// So that the chain "push() pop() push() pop() push() ..." will never result in a chain of reallocations, and only ever 1 reallocation at most

		if(size >= array.length - MAX_REALLOCATION*2 && (size >= array.length / 4 || array.length <= MIN_CAPACITY))
			return;
		
		E old[] = array;

		// We half the capacity typically, but only ever decrease by 32 at most (max_reallocation)
		int capacityDecrease = array.length / 2;
		if(capacityDecrease > MAX_REALLOCATION)
			capacityDecrease = MAX_REALLOCATION;

		// Allocate new list and copy content
		array = (E[])new Object[array.length - capacityDecrease];
		for(int i = 0; i < size; i++)
			array[i] = old[i];

		// Garbage collector will take care of old array
	}

}

