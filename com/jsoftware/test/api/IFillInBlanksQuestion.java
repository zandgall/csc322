package com.jsoftware.test.api;

/**
 * Interface for fill in the blanks questions.  A question may contain one or
 * more blanks.  The answers are a list of words. The answer is correct only if
 * all the keywords match provided by the user matches the correct answer keywords.
 * 
 * @author thaoc
 */

public interface IFillInBlanksQuestion extends IQuestion{
	/**
	 * A fill in the blank question may contain multiple blanks.
	 * Each blank must match each answer.  
	 * @param keywords An array of answer the user provides.
	 * @return 
	 */
	public boolean checkAnswer(String[] keywords);
}
