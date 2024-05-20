/* CSC322 SESSION 3 ASSIGNMENT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## MultipleChoiceQuestion
 # An implementation ot IMultipleChoiceQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

public class MultipleChoiceQuestion implements IMultipleChoiceQuestion {

	private String question;
	private String[] answers;
	private int correct;

	public MultipleChoiceQuestion(String question, String[] answers, int correct) {
		if(answers.length != 4)
			throw new RuntimeException("Multiple Choice only takes exactly 4 answers in array!");
		this.question = question;
		this.answers = answers;
		this.correct = correct;
	}

	public String getQuestion() {
		/** Prints in format:
		* Question:
		*	1)	answers[0]
		*	2)	answers[1]
		*	3)	answers[2]
		*	4)	answers[3]
		*/
		return String.format("%s%n\t1)\t%s%n\t2)\t%s%n\t3)\t%s%n\t4)\t%s%n",
						question, answers[0], answers[1], answers[2], answers[3]);
	}

	public boolean checkAnswer(int index) {
		return index == correct;
	}

}
