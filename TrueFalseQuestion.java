/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## TrueFalseQuestion
 # An implementation of ITrueFalseQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.ITrueFalseQuestion;

public class TrueFalseQuestion implements ITrueFalseQuestion {
	
	private String question;
	private boolean correct;

	public TrueFalseQuestion(String question, boolean correct) {
		this.question = question;
		this.correct = correct;
	}

	public String getQuestion() {
		return "True or False: " + question;
	}

	public boolean checkAnswer(boolean answer) {
		return answer == this.correct;
	}

}

