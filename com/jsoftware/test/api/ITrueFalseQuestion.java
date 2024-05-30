/*
The interface for true false question.  
 */
package com.jsoftware.test.api;

/**
 *
 * @author thaoc
 */
public interface ITrueFalseQuestion extends IQuestion{
		/**
	 * Check if the answer is correct.  
	 * @param answer The user's answer as as a boolean
	 * @return true if correct.
	 */
	public boolean checkAnswer(boolean answer);
}
