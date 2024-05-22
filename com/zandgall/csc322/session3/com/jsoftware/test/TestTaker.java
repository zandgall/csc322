/* CSC322 SESSION 3: ASSIGNMENT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## TestTaker
 # A command line interface to take a test created by TestMaker

 : MADE IN NEOVIM */

package com.jsoftware.test;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.IOException;
import com.jsoftware.test.impl.QuestionFactory;
import com.jsoftware.test.api.IQuestionFactory;
import com.jsoftware.test.api.IQuestionSet;
import com.jsoftware.test.api.IQuestion;
import com.jsoftware.test.api.IMultipleChoiceQuestion;
import com.jsoftware.test.api.ITrueFalseQuestion;
import com.jsoftware.test.api.IFillInBlanksQuestion;
import com.jsoftware.test.api.IShortAnswerQuestion;

public class TestTaker {
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to the TestTaker program!");
		System.out.println();
		System.out.print("What test would you like to take? ");
		Scanner prepScanner = new Scanner(System.in);
		String testName = prepScanner.next();
		IQuestionFactory factory = new QuestionFactory();
		IQuestionSet test = factory.load(testName + ".txt");

		System.out.println();
		System.out.println("Test loaded successfully!");
		System.out.println();

		// Ask about sampling
		System.out.println("Enter 1 to take a whole test or 2 to take a sample test.");
		System.out.print(" > ");
		int type = prepScanner.nextInt();
		while(type != 1 && type != 2) {
			System.out.print("1 for whole test, 2 for sample: ");
			type = prepScanner.nextInt();
		}
		
		// If sampling, sample test
		if(type == 2) {
			System.out.print("How many question would you like to sample? ");
			int sampleSize = prepScanner.nextInt();
			test = test.randomSample(sampleSize);
		}

		// Scoring
		int correct = 0;

		System.out.println("-----------");
		System.out.println("Test begin!");
		System.out.println("-----------");
		// Ask questions
		for(int i = 0; i < test.size(); i++) {

			System.out.println();
			System.out.printf("Question %d of %d%n", i, test.size());
			System.out.println("-----------------");

			IQuestion question = test.getQuestion(i);
			System.out.println(question.getQuestion());
			System.out.print(" > ");
			// We create a scanner for every question, to avoid buffer traffic skipping user input	
			Scanner s = new Scanner(System.in);
			try {
				if(checkAnswer(question, s)) { 
					correct++;
					iSystem.out.println();
					System.out.println("You got it!");
				} else {
					System.out.println();
					System.out.println("Incorrect");
				}
			} catch (InputMismatchException e) {
				System.err.println("Answer given in incorrect format!");
			}
		}

		System.out.printf("You got %d questions right out of %d questions total%n", correct, test.size());
	}

	private static boolean checkAnswer(IQuestion question, Scanner s) throws InputMismatchException {
		if(question instanceof IMultipleChoiceQuestion q)
			return q.checkAnswer(s.nextInt());
		if(question instanceof ITrueFalseQuestion q)
			return q.checkAnswer(s.nextBoolean());
		if(question instanceof IFillInBlanksQuestion q)
			return q.checkAnswer(s.nextLine().split(", ?"));
		if(question instanceof IShortAnswerQuestion q)
			return q.checkAnswer(s.nextLine());

		System.err.println("Unknown question type");
		return false;
	}
}
