/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## TestMaker
 # A command line interface that lets you create and save a test

 : MADE IN NEOVIM */

package com.jsoftware.test;

import com.jsoftware.test.impl.QuestionFactory;
import com.jsoftware.test.api.IQuestionFactory;
import com.jsoftware.test.api.IQuestionSet;
import java.util.Scanner;
import java.io.IOException;

public class TestMaker {

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to the TestMaker program!");
		System.out.println();
		System.out.print("Name of the test: ");

		Scanner s = new Scanner(System.in);
		String testName = s.next();
		IQuestionFactory factory = new QuestionFactory();
		IQuestionSet test = factory.makeEmptyQuestionSet();

		System.out.println();

		while(true) {
			int choice = getChoice(s);
			switch(choice) {
				case 1:
					makeMultipleChoice(factory, test);
					break;
				case 2:
					makeTrueFalse(factory, test);
					break;
				case 3:
					makeFillInBlank(factory, test);
					break;
				case 4:
					makeShortAnswer(factory, test);
					break;
				case 5:
					removeQuestion(factory, test);
					break;
				case 6:
					factory.save(test, testName + ".txt");
					System.out.println("Test saved! Goodbye!");
					return;
			}
		}
	}

	private static int getChoice(Scanner s) {
		System.out.println("What would you like to do?");
		System.out.println("\t1)\tAdd Multiple-choice question");
		System.out.println("\t2)\tAdd True/False question");
		System.out.println("\t3)\tAdd Fill-in-the-blank question");
		System.out.println("\t4)\tAdd Short answer question");
		System.out.println("\t5)\tRemove a question");
		System.out.println("\t6)\tExit");
		System.out.print(" > ");	
		return s.nextInt();
	}

	private static void makeMultipleChoice(IQuestionFactory factory, IQuestionSet test) {
		System.out.println("What is your multiple-choice question?");
		Scanner s = new Scanner(System.in);
		String question = s.nextLine();
		System.out.print("Answer 1: ");
		String a1 = s.next();
		System.out.print("Answer 2: ");
		String a2 = s.next();
		System.out.print("Answer 3: ");
		String a3 = s.next();
		System.out.print("Answer 4: ");
		String a4 = s.next();
		System.out.print("Correct answer (1-4): ");
		int correct = s.nextInt();
		test.add(factory.makeMultipleChoice(question, new String[] {a1, a2, a3, a4}, correct));
	}

	private static void makeTrueFalse(IQuestionFactory factory, IQuestionSet test) {
		System.out.println("What is your true/false question?");
		Scanner s = new Scanner(System.in);
		String question = s.nextLine();
		System.out.print("Correct answer (true/false): ");
		boolean correct = s.nextBoolean();
		test.add(factory.makeTrueFalse(question, correct));
	}

	private static void makeFillInBlank(IQuestionFactory factory, IQuestionSet test) {
		System.out.println("What is your fill-in-the-blanks question?");
		Scanner s = new Scanner(System.in);
		String question = s.nextLine();
		System.out.print("Correct answers (comma, separated, values): ");
		String[] keywords = s.nextLine().split(", ?"); // splits on commas with (optional) space
		test.add(factory.makeFillInBlank(question, keywords));
	}

	private static void makeShortAnswer(IQuestionFactory factory, IQuestionSet test) {
		System.out.println("What is your short-answer question?");
		Scanner s = new Scanner(System.in);
		String question = s.nextLine();
		System.out.print("Number of required keywords in answer: ");

		// Get desired number of keywords, and collect that many in an array
		int numKeywords = s.nextInt();
		String[] keywords = new String[numKeywords];
		for(int i = 0; i < numKeywords; i++) {
			System.out.print("Keyword: ");
			keywords[i] = s.next();
		}
		test.add(factory.makeShortAnswer(question, keywords));
	}

	private static void removeQuestion(IQuestionFactory factory, IQuestionSet test) {
		for(int i = 0; i < test.size(); i++) {
			System.out.println(i + ": "+test.getQuestion(i).getQuestion());
		}
		System.out.print("Which question would you like to remove? ");
		Scanner s = new Scanner(System.in);
		if(!test.remove(s.nextInt()))
			System.out.println("Wasn't able to remove question!");
		else
			System.out.println("Removed question");
	}
}
