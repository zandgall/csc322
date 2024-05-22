/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## QuestionFactory
 #

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IQuestionFactory;
import com.jsoftware.test.api.IQuestion;
import com.jsoftware.test.api.IQuestionSet;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class QuestionFactory implements IQuestionFactory {

	public QuestionFactory() {}

	public IQuestion makeMultipleChoice(String question, String[] choices, int answer) {
		return new MultipleChoiceQuestion(question, choices, answer);
	}

	public IQuestion makeTrueFalse(String question, boolean answer) {
		return new TrueFalseQuestion(question, answer);
	}

	public IQuestion makeFillInBlank(String question, String[] answers) {
		return new FillInBlanksQuestion(question, answers);
	}

	public IQuestion makeShortAnswer(String question, String[] keywords) {
		return new ShortAnswerQuestion(question, keywords);
	}

	private static String[] readKeywords(Scanner s) {
		int numKeywords = s.nextInt();
		String[] keywords = new String[numKeywords];
		for(int i = 0; i < numKeywords; i++)
			keywords[i] = s.next();
		return keywords;
	}

	public IQuestionSet load(String filename) throws IOException {
		IQuestionSet out = makeEmptyQuestionSet();
		File file = new File(filename);
		Scanner s = new Scanner(file);
		while(s.hasNext()) {
			String type = s.next();
			switch(type) {
				case "truefalse":
					out.add(makeTrueFalse(s.nextLine(), s.nextBoolean()));
					break;
				case "blanks":
					out.add(makeFillInBlank(s.nextLine(), readKeywords(s)));
					break;
				case "multiplechoice":
					out.add(makeMultipleChoice(s.nextLine(), new String[]{s.next(), s.next(), s.next(), s.next()}, s.nextInt()));
					break;
				case "short":
					out.add(makeShortAnswer(s.nextLine(), readKeywords(s)));
					break;
				default:
					throw new RuntimeException("Unknown question type \""+type+"\" when loading question set: \"" + filename + "\"");
			}
		}
		return out;
	}

	public boolean save(IQuestionSet testSet, String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			for(int i = 0; i < testSet.size(); i++) {
				IQuestion q = testSet.getQuestion(i);
				if(q instanceof ISaveableQuestion sq)
					sq.write(writer);
				else
					throw new RuntimeException("Some question(s) (" + q.getQuestion() + ") are not able to be written to \"" + filename + "\"!");
			}
			writer.close();
		} catch(IOException e) {
			System.err.println("Could not write \"" + filename + "\"");
			e.printStackTrace();
		}
		return true;
	}

	public IQuestionSet makeEmptyQuestionSet() {
		return new QuestionSet();
	}
}

