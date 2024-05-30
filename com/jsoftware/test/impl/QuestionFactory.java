/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## QuestionFactory
 # A class that is used to produce new questions, along with reading and writing to files

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

	// Question making functions
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

	/**
	* Reads a number, and then reads that many words
	* @param s The scanner input
	*/
	private static String[] readKeywords(Scanner s) {
		int numKeywords = s.nextInt();
		String[] keywords = new String[numKeywords];
		for(int i = 0; i < numKeywords; i++)
			keywords[i] = s.next();
		return keywords;
	}

	/**
	* Loads the implementation questions from a given file
	* @param filename The file to load
	*/
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

	/**
	* Save question set to a file, using the ISaveableQuestion interface
	* @param testSet The question set to write
	* @param filename The file to write to
	*/
	public boolean save(IQuestionSet testSet, String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			for(int i = 0; i < testSet.size(); i++) {
				IQuestion q = testSet.getQuestion(i);
				if(q instanceof ISaveableQuestion sq)
					sq.write(writer);
				else {
					// The question we tried to write is of a class that does not implement ISaveableQuestion
					// This will not be triggered in normal execution, only if a new class is implemented that does NOT implement ISaveableQuestion
					throw new RuntimeException("Some question(s) (" + q.getQuestion() + ") are not able to be written to \"" + filename + "\"!");
				}
			}
			writer.close();
		} catch(IOException e) {
			// Weren't able to open/write to file!
			System.err.println("Could not write \"" + filename + "\"");
			e.printStackTrace();
		}

		return true;
	}

	public IQuestionSet makeEmptyQuestionSet() {
		return new QuestionSet();
	}
}

