/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ShortAnswerQuestion
 # An implementation of IShortAnswerQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IShortAnswerQuestion;
import java.io.IOException;
import java.io.FileWriter;

public class ShortAnswerQuestion implements IShortAnswerQuestion, ISaveableQuestion {
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
		return "Short Answer: " + question;
	}

	public boolean checkAnswer(String answer) {
		// If any keyword is not found in the answer, return incorrect
		for(int i = 0; i < keywords.length; i++)
			if(!answer.toLowerCase().contains(keywords[i]))
				return false;
		return true;
	}

	public void write(FileWriter writer) throws IOException {
		String ls = System.lineSeparator(); // Support "\n" and "\r\n"
		writer.write("short " + question + ls);
		writer.write(keywords.length + " ");
		for(int i = 0; i < keywords.length; i++)
			writer.write(keywords[i] + " ");
		writer.write(ls);
	}
}

