package com.aconex.phonenumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aconex.common.Util;
import com.aconex.dictionary.Dictionary;
import com.aconex.inputtypes.PhoneNumberMatch;

/**
 * PhoneNumberImpl class has various methods like
 * "iterateOverEveryPhoneNumberInList" "displayOutputs" etc which will be called
 * from main class.These methods form the core of the app
 * 
 * @author user
 *
 */
public class PhoneNumberImpl extends PhoneNumber {

	static List<String> storeIntermediateResults = null;
	private static Map<String, String> allLists = null;
	static List<String> tempList = null;

	/**
	 * To getPossibleOutput method iterates over everyphone number ,compares it
	 * with words in dictionary and returns the result map.
	 * 
	 * @param phoneNum
	 * @param bucketedWords
	 * @return
	 */
	@Override
	public HashMap<String, StringBuffer> getPossibleOutputs(Dictionary dic) {

		HashMap<String, StringBuffer> mappedPhToWords = iterateOverEveryPhoneNumberInList(
				super.getListOfRecods(), dic.getBucketsOfPhNoWords());
		return mappedPhToWords;

	}

	/**
	 * iterateOverEveryPhoneNumberInList method takes list of phoneNumbers and
	 * bucketsOfPhNoWords. It iterates over every phone number in list and finds
	 * all possible words for every phoneNumber
	 * 
	 * @param phoneNum
	 * @param bucketedWords
	 * @return
	 */
	public static HashMap<String, StringBuffer> iterateOverEveryPhoneNumberInList(
			List<String> phoneNum, Map<String, List<String>> bucketsOfPhNoWords) {

		// for every phone number in the list,compare with dictionary for words
		int start = 0, end = 0;
		StringBuffer bufferOfFinalResults = null;
		HashMap<String, StringBuffer> mapPhoneNumToWords = new HashMap<String, StringBuffer>();
		for (Iterator<String> iterator = phoneNum.iterator(); iterator
				.hasNext();) {
			// storeIntermediateResults basically stores words for partial
			// string. Eg, say we had 234567 as a phone number input,
			// and say we found match to 234 in dictonary, then below list
			// stores it so that at the end we can use it to merge possible
			// words
			storeIntermediateResults = new ArrayList<String>();
			// allLists is a map which has key like
			// PhoneNumberMatch.FULL_MATCH.toString() or
			// PhoneNumberMatch.PARTIAL_MATCH.toString();
			// PhoneNumberMatch.FULL_MATCH.toString() contains whole match for
			// whole words,while PhoneNumberMatch.PARTIAL_MATCH contains
			// partial combinations.PhoneNumberMatch.PARTIAL_MATCH needs to be
			// looked for all further combinations.
			allLists = new HashMap<String, String>();
			String phoneNumber = (String) iterator.next();
			int length = phoneNumber.length();
			String originalNumber = phoneNumber;
			StringBuffer sb = null;
			allLists = getAllLists(originalNumber, phoneNumber, phoneNumber,
					bucketsOfPhNoWords, start, end, length);

			bufferOfFinalResults = Util.mergeListValues(allLists,
					bucketsOfPhNoWords, phoneNumber);
			String temp = "";
			temp = allLists.get(PhoneNumberMatch.FULL_MATCH.toString());
			tempList = bucketsOfPhNoWords.get(temp);
			sb = Util.getStringBufferFromList(tempList);
			if (sb != null && sb.toString().length() > 0) {
				if (bufferOfFinalResults != null) {
					bufferOfFinalResults.append("~:" + sb.toString());
					mapPhoneNumToWords.put(phoneNumber, bufferOfFinalResults);

				} else {
					mapPhoneNumToWords.put(phoneNumber, sb);
				}
			} else {
				if (bufferOfFinalResults != null) {
					mapPhoneNumToWords.put(phoneNumber, bufferOfFinalResults);
				} else {
					bufferOfFinalResults = new StringBuffer("No MATCH Found");
					mapPhoneNumToWords.put(phoneNumber, bufferOfFinalResults);
				}
			}
		}
		return mapPhoneNumToWords;
	}

	/**
	 * getAllLists methods takes every phone no in list,and iterates it
	 * recursively inside.If phoneNumber is found as a whole match then we will
	 * put it as allLists.put(FULL_MATCH). Incase we dont find it,we will put it
	 * as partial storage allLists.put(PARTIAL_MATCH)
	 * 
	 * @param originalValue
	 * @param valuToCheck
	 * @param phoneNumber
	 * @param bucketedWords
	 * @param start
	 * @param end
	 * @param phLength
	 * @return
	 */
	private static Map<String, String> getAllLists(String originalValue,
			String valuToCheck, String phoneNumber,
			Map<String, List<String>> bucketedWords, int start, int end,
			int phLength) {
		// "fullPhoneNoMatch" stores value if entire phoneNumber is found. i.e
		// say if input no was 123456
		// and if no is found, then the whole match is put in this string.
		String fullPhoneNoMatch = "";
		// "storeIntermediate" stores value if partial subtring of phoneNumber
		// is found. i.e say if input no was 123456
		// and if "123" is found in the step of recursion, then i put this
		// partial string in storeIntermediate variable
		String storeIntermediate = "";

		// valueToChkTmp and phoneNumberTmpl stores value for the next recursive
		// iteration step
		String valueToChkTmp = "";
		String phoneNumberTmpl = "";
		int count = 0;
		if (end >= phLength) {
			return allLists;
		}
		int length = valuToCheck.length();
		// bucketedWords.containsKey(valuToCheck) is true if "complete word" or
		// "partial word" is found in dictionary
		if (bucketedWords.containsKey(valuToCheck)) {
			if (length == phLength)// means phone no completely matched
			{
				if (length == originalValue.length()) {
					fullPhoneNoMatch = valuToCheck;
				} else {
					// partial String matched
					storeIntermediate = valuToCheck;
				}
				valueToChkTmp = Util.generateSubString(phoneNumber, start, end);
				// recursive call
				getAllLists(originalValue, valueToChkTmp, phoneNumber,
						bucketedWords, start, end + 1, phLength);
				if ((storeIntermediate.length()) > 0)
					storeIntermediateResults.add(storeIntermediate);
				if ((fullPhoneNoMatch.length()) > 0)
					allLists.put(PhoneNumberMatch.FULL_MATCH.toString(),
							fullPhoneNoMatch);

			} else {
				storeIntermediate = valuToCheck;
				// phLength = phLength - length;
				phoneNumberTmpl = phoneNumber.substring(length);
				valueToChkTmp = phoneNumberTmpl;
				// recursion
				getAllLists(originalValue, valueToChkTmp, phoneNumberTmpl,
						bucketedWords, 0, 0, phLength - length);
				if (originalValue.trim().length() == phoneNumber.length()) {
					String subDigitMatch = allLists
							.get(PhoneNumberMatch.PARTIAL_MATCH.toString());
					if (subDigitMatch == null) {
						storeIntermediate = "";
						valueToChkTmp = originalValue;
						phoneNumberTmpl = originalValue;
						phLength = phoneNumberTmpl.length();
						getAllLists(originalValue, valueToChkTmp,
								phoneNumberTmpl, bucketedWords, start, end + 1,
								phLength);
					}
				}
				if ((storeIntermediate.length()) > 0)
					storeIntermediateResults.add(storeIntermediate);

			}
		} else {
			valueToChkTmp = Util.generateSubString(phoneNumber, start, end);
			getAllLists(originalValue, valueToChkTmp, phoneNumber,
					bucketedWords, start, end + 1, phLength);
		}
		populateIntermediateResults(storeIntermediate);
		// if "allLists" is null and start and end ==0,that means we have
		// reached
		// a partial string which doesn't match.So lets
		// look for substringmatch.This is done for subsequent digit match
		if (allLists != null && allLists.size() == 0) {
			if (start == end) {
				valueToChkTmp = phoneNumber.substring(1);
				// recursion
				getAllLists(originalValue, valueToChkTmp,
						phoneNumber.substring(1), bucketedWords, 0, 1,
						(phoneNumber.length() - 1));
				count++;
			}
		}

		// came across a scenario where phone number was 8222222 . Basically
		// dictionary had a match for 82 and 222, so what was happening
		// was that it found a match for 82, and 222, and didn't find match for
		// 22, in that case skip
		if ((start == end)
				&& (originalValue.trim().length() == phoneNumber.length())) {
			String subDigitMatch = allLists.get(PhoneNumberMatch.PARTIAL_MATCH
					.toString());
			if (subDigitMatch != null) {
				String str[] = subDigitMatch.split(":");
				if (Util.checkCombinedValueMoreThanOriginalValue(originalValue,
						str)) {
					// check for subset.

					str = Util.checkSubset(str, originalValue.length());
					if (str != null && str.length >= 1) {
						str = str[0].split(":~");
						str = Arrays.stream(str)
								.filter(s -> (s != null && s.length() > 0))
								.toArray(String[]::new);
					}
				}
				str = Util.findDigitsNotMatchedToWord(originalValue, str); // 36653936666
																			// ...[366539,
																			// 366,
																			// 66]

				if (!Util.isNumberValid(originalValue, str)) {
					valueToChkTmp = phoneNumber.substring(1);
					// allLists = new HashMap<String, String>();
					allLists.put(PhoneNumberMatch.PARTIAL_MATCH.toString(), "");

					// recursion
					getAllLists(originalValue, valueToChkTmp,
							phoneNumber.substring(1), bucketedWords, 0, 1,
							(phoneNumber.length() - 1));
				}
			}
		}
		return allLists;
	}

	/**
	 * populateIntermediateResults takes "storeIntermediate" as argument, which
	 * basically contains 'partial string' for "matched word" in dictionary.This
	 * method is called per iteration, and this method collates data for a
	 * particular phoneNumber and puts in "allLists"
	 * 
	 * @param storeIntermediate
	 */
	private static void populateIntermediateResults(String storeIntermediate) {
		int size = storeIntermediateResults.size();
		String tmpStr = "";
		if (storeIntermediateResults != null && size > 0) {
			String key = PhoneNumberMatch.PARTIAL_MATCH.toString();
			if (!(allLists.containsKey(key)))// if map doesn't contain key, then
												// add it to map
			{
				allLists.put(key, storeIntermediate.trim());

			} else {
				// if key already exists, that mena recurseive fn had returns
				// intermediate value and we have to add it to existing map
				// value
				if (storeIntermediate.trim().length() > 0) {
					tmpStr = allLists.get(key);
					allLists.put(key, storeIntermediate.trim() + ":" + tmpStr);
				}

			}

		}
	}

	/**
	 * Takes the resulting map, iterates over every phone number and prints
	 * corresponding words to console.
	 * 
	 * @param mappedPhToWords
	 */
	public void displayOutput(HashMap<String, StringBuffer> mappedPhToWords) {
		if (mappedPhToWords != null) {
			Iterator entries = mappedPhToWords.entrySet().iterator();
			String str[];
			String str1[];

			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				Object key = thisEntry.getKey();
				Object value = thisEntry.getValue();
				str = (value.toString()).split("~:");
				System.out
						.println("--------------------------------------------------------------");
				System.out.println("Phone num is" + key
						+ "and combinations to choose from are");
				for (int i = 0; i < str.length; i++) {
					str1 = str[i].split("~$");
					for (int j = 0; j < str1.length; j++) {
						System.out.print(str1[j].toUpperCase());
						System.out.println("");
					}
				}

			}
			System.out.println("---------------------------------------------");
		}

	}

	@Override
	public void loadRecords(List<String> list) {
		super.loadRecords(list);
	}

}
