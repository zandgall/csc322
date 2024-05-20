/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## FillInTheBlanksQuestion
 # An implementation of IFillInTheBlanksQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test;

public class FillInTheBlanksQuestion implements IFillInTheBlanksQuestion {
	
	private String question;
	private String[] keywords;

	public FillInTheBlanksQuestion(String question, String[] keywords) {
		this.question = question;
		// cleanse input keywords (strip and lowercase)
		this.keywords = new String[keywords.length];
		for(int i = 0; i < keywords.length; i++)
			this.keywords[i] = keywords[i].strip().toLowerCase();
	}

	public String getQuestion() {
		return question;
	}

	public boolean checkAnswers(String[] keywords) {
		if(keywords.length!=this.keywords.length)
			return false;

		// Check if any of the keywords in lowercase and stripped don't match, in which case return incorrect
		for(int i = 0; i < keywords.length; i++) {
			if(keywords[i].strip().toLowerCase()!=this.keywords[i])
				return false;
		}
		return true;
	}

}

