/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ShortAnswerQuestion
 # An implementation of IShortAnswerQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IShortAnswerQuestion;

public class ShortAnswerQuestion implements IShortAnswerQuestion {
	private String question;
	private String[] keywords;

	public ShortAnswerQuestion(String question, String[] keywords) {
		this.question = question;
		// Cleanse input keywords (strip and lowercase)
		this.keywords = new String[keywords.length];
		for(int i = 0; i < keywords.length; i++)
			this.keywords[i] = keywords[i].strip().toLowerCase();
	}

	public String getQuestion() {
		return question;
	}

	public boolean checkAnswer(String answer) {
		// If any keyword is not found in the answer, return incorrect
		for(int i = 0; i < keywords.length; i++)
			if(!answer.toLowerCase().contains(keywords[i]))
				return false;
		return true;
	}
}

