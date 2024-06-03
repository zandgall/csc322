/* CSC322 SESSION 3 ASSIGNMENT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## QuestionSet
 # The implementation of IQuestionSet.

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import com.jsoftware.test.api.IQuestion;
import com.jsoftware.test.api.IQuestionSet;

public class QuestionSet implements IQuestionSet {

	private ArrayList<IQuestion> questionSet;

	public QuestionSet() {
		questionSet = new ArrayList<IQuestion>();
	}

	@Override
	public IQuestionSet emptyTestSet() {
		return new QuestionSet();
	}

	/**
	* Produces a sample question set that randomizes in a way that produces minimal repeats
	* @param size The number of questions to be produced in the sample
	*/
	@Override
	public IQuestionSet randomSample(int size) {
		if(size==0)
			return emptyTestSet();
		if(this.questionSet.isEmpty())
			throw new RuntimeException("Attempting to sample an empty QuestionSet!");

		QuestionSet output = new QuestionSet();
		
		// Create a list of indices and shuffle them to avoid repeats
		ArrayList<Integer> sample = new ArrayList<Integer>();
		for(int i = 0; i < this.size(); i++)
			sample.add(i);

		for(int i = 0; i < size; i++) {
			// In this list we select an indice from "sample" using (i % this.size())
			// This causes the sample list to functionally repeat if we extend past the end of it
			// If we are at the beginning of one of these repititions, we shuffle the list
			if(i % this.size() == 0)
				Collections.shuffle(sample);
			output.add(getQuestion(sample.get(i % this.size())));
		}

		return output;
	}

	@Override
	public boolean add(IQuestion question) {
		return questionSet.add(question);
	}

	@Override
	public boolean remove(int index) {
		if(index < 0 || index >= size())
			return false;
		questionSet.remove(index);
		return true;
	}

	@Override
	public IQuestion getQuestion(int index) {
		if(index < 0 || index >= size())
			return null;
		return questionSet.get(index);
	}

	@Override
	public int size() {
		return questionSet.size();
	}
}
