/* CSC322 SESSION 3 ASSIGNMENT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## MultipleChoiceQuestion
 # An implementation ot IMultipleChoiceQuestion

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IMultipleChoiceQuestion;
import java.io.IOException;
import java.io.FileWriter;

public class MultipleChoiceQuestion implements IMultipleChoiceQuestion, ISaveableQuestion {

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
		return String.format("Multiple Choice: %s%n\t1)\t%s%n\t2)\t%s%n\t3)\t%s%n\t4)\t%s%n",
						question, answers[0], answers[1], answers[2], answers[3]);
	}

	public boolean checkAnswer(int index) {
		return index == correct;
	}

	// Used to write question to files
	public void write(FileWriter writer) throws IOException {
		String ls = System.lineSeparator(); // Support "\n" and "\r\n"
		writer.write("multiplechoice " + question + ls);
		writer.write(answers[0] + " " + answers[1] + " " + answers[2] + " " + answers[3] + " " + correct + ls);
	}

}
