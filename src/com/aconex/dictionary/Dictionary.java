/**
 * 
 */
package com.aconex.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aconex.common.Util;
import com.aconex.factory.Records;

/**
 * Dictionary is an abstract class which has methods to loadRecords i.e
 * loadDictinaryWords into memory and populateBucketOfPhNoOfWords which will map
 * words with similar numbers in one bucket.
 * 
 * @author user
 *
 */
public class Dictionary implements Records {

	public Map<String, List<String>> bucketsOfPhNoWords = new HashMap<String, List<String>>();
	List<String> listOfRecods = new ArrayList<String>();

	public void populateBucketsOfPhNoWords() {
		bucketsOfPhNoWords = Util.populatedBucketedWords(listOfRecods);
	}

	@Override
	public void loadRecords(List<String> list) {
		this.listOfRecods = list;
	}

	public Map<String, List<String>> getBucketsOfPhNoWords() {
		return bucketsOfPhNoWords;
	}

	public void setBucketsOfPhNoWords(
			Map<String, List<String>> bucketsOfPhNoWords) {
		this.bucketsOfPhNoWords = bucketsOfPhNoWords;
	}
}
