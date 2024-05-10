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

		assert a.equals(b);  // a and b have the same content
		assert a != b;       // == does not invoke .equals() - a!=b despite having same content
		assert !a.equals(c); // b and c do not have the same content
		assert !c.equals(d); // c and d do not have the same content
		assert !d.equals(e); // d and e do not have the same content
		assert e.equals(f);  // e and f have the same content
	}
}
