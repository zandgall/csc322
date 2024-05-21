/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## QuestionFactory
 #

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import com.jsoftware.test.api.IQuestionFactory;

public class QuestionFactory implements IQuestionFactory {
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

	public IQuestionSet load(String filename) throws IOException {

	}

	public boolean save(IQuestionSet testSet, String filename) {
		
	}
}

