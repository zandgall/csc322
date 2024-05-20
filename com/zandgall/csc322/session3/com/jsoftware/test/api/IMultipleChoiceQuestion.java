package com.jsoftware.test.api;

/**
 * Interface for multiple choice question.  A multiple choice question has 
 * a question and a list of choices.  To check if a user has select the
 * correct choice, the choice is convert to an index and is matched to the
 * correct index in the question.
 * 
 * @author thaoc
 */
public interface IMultipleChoiceQuestion extends IQuestion{
	
	/**
	 * Check answer.
	 * @param index The index of the choice.
	 * @return  True if answer is correct.
	 */
	public boolean checkAnswer(int index);
}
