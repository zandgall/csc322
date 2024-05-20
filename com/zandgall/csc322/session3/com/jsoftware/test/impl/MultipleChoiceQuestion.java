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
		
	}

	public boolean checkAnswer(int index) {
		return index == correct;
	}

}
