/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## FillInBlanksQuestion
 # An implementation of IFillInBlanksQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IFillInBlanksQuestion;
import java.io.IOException;
import java.io.FileWriter;

public class FillInBlanksQuestion implements IFillInBlanksQuestion, ISaveableQuestion {
	
	private String question;
	private String[] keywords;

	public FillInBlanksQuestion(String question, String[] keywords) {
		this.question = question;
		// cleanse input keywords (strip and lowercase)
		this.keywords = new String[keywords.length];
		for(int i = 0; i < keywords.length; i++)
			this.keywords[i] = keywords[i].strip().toLowerCase();
	}

	public String getQuestion() {
		return "Fill in the blank: " + question + " (comma, separated, values)";
	}

	public boolean checkAnswer(String[] keywords) {
		if(keywords.length!=this.keywords.length)
			return false;

		// Check if any of the keywords in lowercase and stripped don't match, in which case return incorrect
		for(int i = 0; i < keywords.length; i++)
			if(!keywords[i].strip().toLowerCase().equals(this.keywords[i]))
				return false;

		return true;
	}
	
	public void write(FileWriter writer) throws IOException {
		String ls = System.lineSeparator(); // support \n and \r\n
		writer.write("blanks ");
		writer.write(question + ls);
		writer.write(keywords.length + " ");
		for(String s : keywords)
			writer.write(s + " ");
		writer.write(ls);
	}


}

