package com.jsoftware.test.api;

/**
 *
 * @author thaoc
 */
public interface  IQuestion {
	/**
	 * Return a formatted question as a string.  It should include options if
	 * the question is a multiple choice or true/false question.  
	 * 
	 * Multiple choice example
	 * Which of the following is not a primitive type in Java?
	 *    1)  double
	 *    2)  int
	 *    3)  String
	 *    4)  boolean
	 * 
	 * True false example:
	 * int is not a primitive data type in Java.  True/False?
	 */
	public String getQuestion();

}
