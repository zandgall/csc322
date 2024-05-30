package com.jsoftware.test.api;

/**
 *
 * @author thaoc
 */
public interface IShortAnswerQuestion extends IQuestion{
	/**
	 * Check answer for short answer question.
	 * @param answer The user's answer as as a boolean
	 * @return true if correct.
	 */
	public boolean checkAnswer(String answer);
}
