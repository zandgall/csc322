/* CSC322 SESSION 3: EXERCISE - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Parabola
 # An interface to use the Rational class to generate the vertex form of a parabola

 ## Rational
 # An extension of 'Number' that keeps a numerator and denominator, with functions to modify and simplify
 # Largely inspired on the example given in the book - but all my own work and thinking
 # This is sort of a Monad, you pass in values from the outside, magic stuff happens on the inside, and you can pull out a value after several transformations

 : MADE IN NEOVIM */

package com.zandgall.csc322.session3.exercise;

import java.util.Scanner;

public class Parabola {
	private static class Rational extends Number {
		private Long numerator, denominator;

		private Rational(Long numerator, Long denominator) {
			this.numerator = numerator;
			this.denominator = denominator;
		}

		/** Number abstract functions **/
		@Override
		public long longValue() { return numerator.longValue() / numerator.longValue(); }

		@Override
		public int intValue() { return numerator.intValue() / denominator.intValue(); }

		@Override
		public float floatValue() { return numerator.floatValue() / denominator.floatValue(); }

		@Override 
		public double doubleValue() { return numerator.doubleValue() / denominator.doubleValue(); }
		/*******************************/

		/**
		 * Creates a Rational number with the given numerator, and 1 as the denominator
		 */
		public static Rational create(long numerator) {
			return new Rational(numerator, 1l);
		}

		/**
		 * Multiplies the denominator by the divisor and return the result
		 */
		public Rational divide(long divisor) {
			return new Rational(numerator, denominator * divisor);
		}

		/**
		 * Multiplies the numerator by the divisor and return the result
		 */
		public Rational multiply(long multiplier) {
			return new Rational(numerator * multiplier, denominator);
		}

		/**
		 * Add the adder * denominator to the numerator and return the result
		 */
		public Rational add(long adder) {
			return new Rational(numerator + adder * denominator, denominator);
		}

		/**
		 * Run add with the negative input, returns the result
		 */
		public Rational subtract(long subtractor) {
			return add(-subtractor);
		}

		/**
		 * Find the greatest common demoninator and divide both the numerator and denominator by it, returning the result
		 */
		public Rational simplify() {
			long GCD = 1;

			for(long i = 2; i <= Math.abs(numerator) && i <= Math.abs(denominator); i++)
				if(numerator % i == 0 && denominator % i == 0)
					GCD = i;

			return new Rational(numerator / GCD, denominator / GCD);
		}

		/**
		 * Return a string in the format "(numerator) / (denominator)"
		 */
		@Override
		public String toString() {
			return String.format("%d / %d", numerator.longValue(), denominator.longValue());
		}

	}

	public static void main(String[] args) {
		System.out.print("Enter a, b, c: ");
		Scanner s = new Scanner(System.in);
		long a = s.nextLong();
		long b = s.nextLong();
		long c = s.nextLong();
		// Using verbose functions to prove they work
		Rational h = Rational.create(-b).divide(2).divide(a).simplify();
		Rational k = Rational.create(4).multiply(a).multiply(c).subtract(b*b).divide(4).divide(a).simplify();
		// You could also just write them out as:
		// Rational h = Rational.create(-b).divide(2*a).simplify();
		// Rational k = Rational.create(4*a*c-b*b).divide(4*a).simplify();
		System.out.printf("h is %s, and k is %s%n", h.toString(), k.toString());
	}
}

