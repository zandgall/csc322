package com.zandgall.csc322.session2.discussion;

public class Comparison {
	// A simple class with three values that will be used to determine if two instances are equivalent in value 
	private static class CompClass {
		protected String name;
		protected int value;
		protected float weight;

		public CompClass(String name, int value, float weight) {
			this.name = name;
			this.value = value;
			this.weight = weight;
		}

		// Check if 'other' is an instance of CompClass. Creating 'comp', which is the same as `(CompClass)other` using it to check if the values are the same.
		@Override
		public boolean equals(Object other) {
			if(other instanceof CompClass comp)
				return comp.name.equals(this.name) && comp.value == this.value && comp.weight == this.weight;
			return false;
		}
	}

	public static void main(String[] args) {
		// Create some CompClass instances, some with same content, some with variations
		CompClass a = new CompClass("Hello", 4, 3.4f); // a == b
		CompClass b = new CompClass("Hello", 4, 3.4f);

		CompClass c = new CompClass("Hello", 4, 3.2f); // different weight
		CompClass d = new CompClass("Hello", 3, 3.4f); // different value

		CompClass e = new CompClass("World", 2, 6.8f); // e == f
		CompClass f = new CompClass("World", 2, 6.8f);

		System.out.println("A and B have the same content: "+a.equals(b)+" (should match 'true')");
		System.out.println("A==B (does not use .equals()): "+(a==b)+" (should match 'false')");
		System.out.println("A and C have the same content: "+a.equals(c)+" (should match 'false')");
		System.out.println("C and D have the same content: "+c.equals(d)+" (should match 'false')");
		System.out.println("D and E have the same content: "+d.equals(e)+" (should match 'false')");
		System.out.println("E and F have the same content: "+e.equals(f)+" (should match 'true')");
		
		// Asserting all the statements we just printed - add '-ea' as command line argument
		assert a.equals(b) == true;
		assert ( a == b )  == false;
		assert a.equals(c) == false;
		assert c.equals(d) == false;
		assert d.equals(e) == false;
		assert e.equals(f) == true;
		
		System.out.println("All tests passed.");
	}
}
