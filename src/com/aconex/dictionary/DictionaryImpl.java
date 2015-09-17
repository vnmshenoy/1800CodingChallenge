package com.aconex.dictionary;

import java.util.List;

/**
 * DictionaryImpl stores words from dictionary and also creates a mao which will
 * store all phone numbers corresponding to words.Threfore, when given a
 * phonenumber, it would be easier to search the possible words from
 * bucketsOfPhNoWords
 * 
 * @author user
 *
 */
public class DictionaryImpl extends Dictionary {

	List<String> dictionaryWords;

	@Override
	public void loadRecords(List<String> list) {
		super.loadRecords(list);
	}

	public List<String> getDictionaryWords() {
		return dictionaryWords;
	}

	public void setDictionaryWords(List<String> dictionaryWords) {
		this.dictionaryWords = dictionaryWords;
	}

}
