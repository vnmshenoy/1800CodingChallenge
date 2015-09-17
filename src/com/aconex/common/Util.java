package com.aconex.common;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aconex.inputtypes.NumberType;
import com.aconex.inputtypes.PhoneNumberMatch;

/**
 * Util class has static resusbale methods which can be used by multiple classes
 * 
 * @author user
 *
 */
public class Util {

	public static final String IS_FILE_REGEX = "^[\\w,\\s-]+\\.[A-Za-z]{3}$";
	public static final String LOAD_DEFAULT = "dict.txt";
	public static final String PHONE = "phone";
	public static final String DICTIONARY = "dictionary";
	static List<String> tempList = null;

	/**
	 * This method reads arguments entered by user from commandline and sees if
	 * it is a normal STDIN or a file input.Based on whether the input is a FILE
	 * or normal String input it populates the map & returns it. Caller of this
	 * method will have to retrieve the values of the map.It loads files for
	 * both dictionary and phone numbers
	 * 
	 * EG user enters java main phoneNoFile1.txt will return
	 * map<FILE_INPUT_PHONE,<abc.txt> Similarlyif user enter EG user enters java
	 * main -d dic.txt will return map<FILE_INPUT_DICTIONARY,<dic.txt>
	 * 
	 * If -d not present in params will return
	 * map<FILE_INPUT_DICTIONARY,<default.txt>
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static Map<String, List<String>> readArguments(String[] args)
			throws FileNotFoundException {
		List<String> tempList = new ArrayList<String>();
		List<String> tempList1 = new ArrayList<String>();
		String tmp = NumberType.FILE_INPUT_PHONE.toString();
		String tmp1 = NumberType.FILE_INPUT_DICTIONARY.toString();
		boolean isDefaultDictionary = true;
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < args.length; i++) {
			if (isInputAFile(args[i])) {

				tempList.add(args[i]);
				map.put(tmp, tempList);
			} else if (!(args[i].contains("-d"))) {
				tmp = NumberType.STRING_INPUT.toString();
				tempList.add(args[i]);
			} else if (args[i].contains("-d")) {
				// this if condition to see if user gave -d option to set his
				// own dictionary
				isDefaultDictionary = false;
				tempList1.add(args[i].substring(3));
				map.put(tmp1, tempList1);

			}
		}

		if (isDefaultDictionary) {
			tempList1.add(LOAD_DEFAULT);
			map.put(tmp1, tempList1);
		}

		map.put(tmp, tempList);
		return map;
	}

	/**
	 * isInputAFile determines if given input is a File or a normal text. It
	 * uses regex to determine the same. For eg, "file1.txt" is a file, while on
	 * the other hand a simple input like "222" would be a String input.
	 * 
	 * @param input
	 * @return
	 */
	private static boolean isInputAFile(String input) {
		boolean isFile = false;
		Pattern pattern = Pattern.compile(IS_FILE_REGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			isFile = true;
		}
		return isFile;
	}

	/**
	 * loadNumberEncoding() method loads number encoding on the phone that our
	 * application uses. DIGIT - CHARACTERS 2 ABC 3 DEF 4 GHI 5 JKL 6 MNO 7 PQR
	 * 8 TUV 9 WXYZ
	 * 
	 * @return
	 */
	private static Map<String, String> loadNumberEncoding() {
		Map<String, String> encodingMap = new HashMap<String, String>();
		encodingMap.put("A", "2");
		encodingMap.put("B", "2");
		encodingMap.put("C", "2");
		encodingMap.put("D", "3");
		encodingMap.put("E", "3");
		encodingMap.put("F", "3");
		encodingMap.put("G", "4");
		encodingMap.put("H", "4");
		encodingMap.put("I", "4");
		encodingMap.put("J", "5");
		encodingMap.put("K", "5");
		encodingMap.put("L", "5");
		encodingMap.put("M", "6");
		encodingMap.put("N", "6");
		encodingMap.put("O", "6");
		encodingMap.put("P", "7");
		encodingMap.put("Q", "7");
		encodingMap.put("R", "7");
		encodingMap.put("S", "7");
		encodingMap.put("T", "8");
		encodingMap.put("U", "8");
		encodingMap.put("V", "8");
		encodingMap.put("W", "9");
		encodingMap.put("X", "9");
		encodingMap.put("Y", "9");
		encodingMap.put("Z", "9");
		return encodingMap;

	}

	/**
	 * populatedBucketedWords goes through every word in dictionary.For every
	 * word in dictionary it creates a corresponding number. So this method will
	 * return a map,with key as number and value as a list of all possible words
	 * for that number.
	 * 
	 * eg say dictionary had "CAR" and "CAP",then this map will have one of the
	 * keys as 227,[CAR,CAP] eg {23=[AD], 56=[JM], 222=[AAA, ABC], 2255=[CALL],
	 * 222222=[AACAAA], 222333=[ABCDEF], 63=[ME]}
	 * 
	 * @param dictionary
	 * @return
	 */
	public static Map<String, List<String>> populatedBucketedWords(
			List<String> dictionary) {
		StringBuffer bucketedListKey = null;
		Map<String, List<String>> bucketedWords = new HashMap<String, List<String>>();
		Map<String, String> encodingMap = loadNumberEncoding();
		List<String> dictionaryWordListOrganised = null;
		// iterate through every word in dictionary
		for (Iterator iterator = dictionary.iterator(); iterator.hasNext();) {
			String dictionaryWord = (String) iterator.next();
			bucketedListKey = new StringBuffer();
			// iterate through every letter in word and create key
			for (int i = 0; i < dictionaryWord.length(); i++) {
				char c = dictionaryWord.charAt(i);
				String cStr = String.valueOf(c);
				String num = encodingMap.get(cStr.toUpperCase());
				bucketedListKey.append(num);
			}
			// add values to map
			if (bucketedWords == null || bucketedWords.isEmpty()) {
				dictionaryWordListOrganised = new ArrayList<String>();
				dictionaryWordListOrganised.add(dictionaryWord);
				bucketedWords.put(bucketedListKey.toString(),
						dictionaryWordListOrganised);
			} else {
				String buckWordsKey = bucketedListKey.toString();
				if (bucketedWords.containsKey(buckWordsKey)) {
					dictionaryWordListOrganised = bucketedWords
							.get(buckWordsKey);
					dictionaryWordListOrganised.add(dictionaryWord);
					bucketedWords.put(bucketedListKey.toString(),
							dictionaryWordListOrganised);
				} else {
					dictionaryWordListOrganised = new ArrayList<String>();
					dictionaryWordListOrganised.add(dictionaryWord);
					bucketedWords.put(bucketedListKey.toString(),
							dictionaryWordListOrganised);
				}
			}
		}
		return bucketedWords;
	}

	/**
	 * mergeListValues takes "allLists" & "bucketedWords" as input.allLists is a
	 * map which has values for PARTIAL_MATCH and FULL_MATCH This method focuses
	 * on values for "PARTIAL_MATCH".For eg for phone number 225563, there are
	 * two partial matches in "2255" and "63"( i.e CALL and ME). subDigitMatch
	 * will have value like 2255:63 (digits are separated by :).This method
	 * splits based on ":" and in turn calls mergeIntermediateLists to find word
	 * combinations
	 * 
	 * @param allLists
	 * @param bucketedWords
	 * @return
	 */
	public static StringBuffer mergeListValues(Map<String, String> allLists,
			Map<String, List<String>> bucketedWords, String originalValue) {
		String subDigitMatch = allLists.get(PhoneNumberMatch.PARTIAL_MATCH
				.toString());
		String[] originalValueStr = null;
		String[] indexesOfNotMatchedDigits = null;
		StringBuffer sb = null;
		if (subDigitMatch != null && subDigitMatch.contains(":")) {
			String str[] = subDigitMatch.split(":");
			List<String> tmpList = new ArrayList<String>();
			String str1[] = null;
			StringBuffer sb1 = new StringBuffer();
			/**
			 * in some cases we get matches for whole word as well as substring.
			 * In those case combination of all possible words would be greater
			 * than original phone.Let's take an eg if say phone no is 22554563,
			 * and you may get sub digits as "225", "54", "563" ,"56". Here if
			 * you see combination of all 4 groups is longer that original
			 * no(8). For handling above scenario, let's take 2 scenarios,
			 * 1)225,54,563 and 2)225,54,56
			 * 
			 */
			boolean subset = false;
			if (checkCombinedValueMoreThanOriginalValue(originalValue, str)) {
				// check for subset.
				subset = true;
				str = checkSubset(str, originalValue.length());
				if (str != null && str.length >= 1) {
					str = str[0].split("~:");
					str = Arrays.stream(str)
							.filter(s -> (s != null && s.length() > 0))
							.toArray(String[]::new);
				}
			}
			str1 = findDigitsNotMatchedToWord(originalValue, str);
			indexesOfNotMatchedDigits = getIndexesOfDigitsNotMatched(
					originalValue, str1);
			boolean isValid = isNumberValid(originalValue, str1);
			if (!isValid)
				return null;

			for (int i = 0; i < str.length; i++) {

				tmpList.add(str[i]);
			}

			sb = mergeIntermediateLists(bucketedWords, tmpList);
			sb = mergeSingleNotMatchedDigit(sb, indexesOfNotMatchedDigits, str1);

		}
		return sb;
	}

	/**
	 * checkSubset takes the string array,checks if the number sub part already
	 * exists
	 * 
	 * @param str1
	 * @param size
	 * @return
	 */
	public static String[] checkSubset(String[] str1, int size) {

		int val = 0;
		String[] result = null;
		List<String> list1 = new ArrayList<String>();
		StringBuffer subSet = new StringBuffer();
		int i = 0;
		int val1 = 0;
		int currentSum = 0;
		val = str1[i].length();
		currentSum = val;
		subSet.append(str1[i]);
		for (int j = 0; j < str1.length; j++) {

			if (i != j) {
				val1 = str1[j].length();
				currentSum += val1;
				subSet.append(":~");
				subSet.append(str1[j]);
				if (currentSum == size) {
					list1.add(subSet.toString());
					currentSum = val;
					subSet.delete(0, subSet.length());// clear the string buffer
					subSet.append(str1[i]);
				}
			}

		}
		str1 = list1.stream().toArray(String[]::new);
		return str1;
	}

	/**
	 * checkCombinedValueMoreThanOriginalValue sees that if the combined value
	 * of all possible combinations of a phone number is more than original
	 * length of phone number. For instance, say ph no is 254378 , and words
	 * correponding to it are say 2,254,43,378. Therefore, combinations of all
	 * subparts is more than original value
	 * 
	 * @param originalValue
	 * @param str1
	 * @return
	 */
	public static boolean checkCombinedValueMoreThanOriginalValue(
			String originalValue, String[] str1) {
		int count = 0;
		boolean flag = false;
		for (int i = 0; i < str1.length; i++) {
			count += str1[i].length();
		}
		if (count > originalValue.length()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * @param sb
	 * @param indexesOfNotMatchedDigits
	 * @param str1
	 * @return
	 */
	private static StringBuffer mergeSingleNotMatchedDigit(StringBuffer sb,
			String[] indexesOfNotMatchedDigits, String[] str1) {
		String[] tempArr = null;
		if (sb != null)
			tempArr = sb.toString().split("~:");
		if ((str1 != null) && (indexesOfNotMatchedDigits != null)) {
			indexesOfNotMatchedDigits = Arrays
					.stream(indexesOfNotMatchedDigits)
					.filter(s -> (s != null && s.length() > 0))
					.toArray(String[]::new);
			str1 = Arrays.stream(str1)
					.filter(s -> (s != null && s.length() > 0))
					.toArray(String[]::new);
		}
		StringBuffer tempBuffer = new StringBuffer();
		if (str1 != null) {
			for (int k = 0; k < tempArr.length; k++) {
				String z = tempArr[k];
				for (int i = 0; i < str1.length; i++) {
					for (int j = 0; j < indexesOfNotMatchedDigits.length; j++) {
						String temp = indexesOfNotMatchedDigits[j];
						String temp1 = str1[i].trim();
						int index = 0;
						if (temp != null && temp.trim().length() > 0
								&& temp1.length() > 0) {
							index = Integer.parseInt(temp);
							String value = str1[j];
							z = z.substring(0, index + 1) + value
									+ z.substring(index + 1, z.length());
						}
					}
					tempBuffer.append(z);
					tempBuffer.append("~:");
					break;
				}
			}
		} else {
			tempBuffer = sb;
		}
		return tempBuffer;
	}

	/**
	 * getIndexesOfDigitsNotMatched gets indexes of the digits whose match is
	 * not found in dictionary.
	 * 
	 * @param originalValue
	 * @param str1
	 */
	public static String[] getIndexesOfDigitsNotMatched(String originalValue,
			String[] str1) {
		if (str1 != null) {
			String[] temp = new String[str1.length];
			for (int i = 0; i < str1.length; i++) {
				int index = 0;
				if (str1[i].trim().length() > 0) {
					index = originalValue.indexOf(str1[i]);
					temp[i] = new Integer(index).toString();
				}
			}
			return temp;
		}
		return null;
	}

	/**
	 * isNumberValid validates output.It ensures that no more than one
	 * digit(consecutive) of phone number is left as is.If two consecutive
	 * digits are left as is, that means number is not mapped and therefore it
	 * will return false.So caller of this method based on the value will
	 * process accordingly. For instance, if false is returned to the caller,
	 * then caller will show no match found. digit(consecutive) is left
	 * 
	 * @param originalValue
	 * @param str
	 * @return
	 */
	public static boolean isNumberValid(String originalValue, String[] str1) {
		boolean flag = true;
		if (str1 != null) {
			for (int i = 0; i < str1.length; i++) {
				if (str1[i].length() > 1) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 
	 * 
	 String[] str1; StringBuffer sb1; for (int i = 0; i < str.length; i++) {//
	 * str 2255 63 45 str1 = originalValue.split(str[i].trim()); sb1 = new
	 * StringBuffer(); // --->sb1-6377 for (int j = 0; j < str1.length; j++) {
	 * if (i == (str.length - 1)) { if (str1[j].length() > 1) { return false; }
	 * } else if (str1[j].length() > 0) { sb1.append(str1[j].trim()); }
	 * 
	 * } originalValue = sb1.toString(); } return true;
	 */
	/**
	 * @param originalValue
	 * @param str
	 * @return
	 */
	public static String[] findDigitsNotMatchedToWord(String originalValue,
			String[] str) {
		String[] str1 = null;
		StringBuffer sb1;
		for (int i = 0; i < str.length; i++) {// str 2255 63 45
			str1 = originalValue.split(str[i].trim());
			sb1 = new StringBuffer();
			if (str1 != null && str1.length > 0) {
				for (int j = 0; j < str1.length; j++) {
					if (i == (str.length - 1)) {
						return str1;
					} else if (str1[j].length() > 0) {
						sb1.append(str1[j].trim());
					}
					originalValue = sb1.toString();
				}
			}
		}
		return null;
	}

	/**
	 * mergeIntermediateLists method receives list of partial string(digits) eg
	 * 2255,63. This method will iterate over the list items. It takes first two
	 * items from list and finds correspoding list of words from dictionary.For
	 * each words, it finds combinations. Word boundaries are separated by "-".
	 * In above case "tempList" for firstNumber will be "CALL" and "templList"
	 * for secondNumber will be "ME"
	 * 
	 * @param bucketedWords
	 * @param tmpList
	 * @param indexesOfNotMatchedDigits
	 * @return
	 */
	public static StringBuffer mergeIntermediateLists(
			Map<String, List<String>> bucketedWords, List<String> tmpList) {

		int size = tmpList.size();
		if (size >= 1) {
			try {
				String firstElement = tmpList.get(0);
				tempList = new ArrayList<String>();
				StringBuffer sb = null;
				StringBuffer sb2 = null;
				if (tmpList != null && size > 1) {
					String firstNumber = tmpList.get(0);
					String secondNumber = tmpList.get(1);
					if (size >= 2) {
						tmpList = tmpList.subList(2, size);
					}
					// update string buffer object sb and sb2 from list which we
					// got
					// from bucketeedWords.For eg,
					// if bucketedWords.get(firstNumber)--> AAA,BBB will be
					// converted
					// to AAA~:BBB
					Iterator<String> iterator = tmpList.iterator();
					tempList = bucketedWords.get(firstNumber);
					sb = getStringBufferFromList(tempList);
					tempList = bucketedWords.get(secondNumber);
					sb2 = getStringBufferFromList(tempList);
					sb2 = findWords(sb, sb2, iterator, bucketedWords);

				}
				if (size == 1) {
					sb2 = new StringBuffer();
					sb2.append(firstElement);
				}
				return sb2;
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(bucketedWords + ">>" + tmpList);

			}
		}
		return null;

	}

	/**
	 * getStringBufferFromList takes a list for every partial digit match, and
	 * separates each word with ~: For eg,This method got "AAA,BBB" as
	 * parameter, then it will be converted to AAA~:BBB. Note: say AAA,BBB" was
	 * output of bucketedWords.get(firstNumber);
	 * 
	 * @param tempList
	 * @param sb
	 */
	public static StringBuffer getStringBufferFromList(List<String> tempList) {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		if (tempList != null) {
			for (String string : tempList) {
				sb.append(string);
				if (count < (tempList.size() - 1))
					sb.append("~:");
				count++;
			}
		}
		return sb;
	}

	/**
	 * @param phoneNumber
	 * @param start
	 * @param end
	 * @return
	 */
	public static String generateSubString(String phoneNumber, int start,
			int end) {
		String valuToCheck;
		int size = phoneNumber.length();
		if (start == end) {
			valuToCheck = phoneNumber.substring(start, start + 1);

		} else {
			if (!(end >= size))
				valuToCheck = phoneNumber.substring(start, end + 1);
			else {
				valuToCheck = phoneNumber.substring(start, end);
			}

		}
		return valuToCheck;
	}

	/**
	 * findWords method takes two stringBuffer objects. Each StringBuffers
	 * object has ~: separators which separates words. For instance sb can be
	 * "AAA~:BBB" and sb2 can be "DDD~:EEE". This method will iterate over each
	 * element in "sb" and find combinations. For instance it will find
	 * "AAA-DDD" "AAA-EEE" etc. Note: Imagine AAA,DDD,BBB,EEE are all valid
	 * valids in dictionary
	 * 
	 * @param sb
	 * @param sb2
	 * @param iterator
	 * @param bucketedWords
	 * @return
	 */
	private static StringBuffer findWords(StringBuffer sb, StringBuffer sb2,
			Iterator<String> iterator, Map<String, List<String>> bucketedWords) {

		StringBuffer intermediateWordBuffer = new StringBuffer();
		String[] str1 = sb.toString().split("~:");
		String[] str2 = sb2.toString().split("~:");
		int count = 0;
		// iterate over everty string array for elements from sb.Separate each
		// word with ~: .For eg,in our case it will send "AAA" as string and
		// "DDD,EEE" as str2
		for (String string : str1) {
			intermediateWordBuffer.append(calculateCombinations(string, str2));
			if (count < ((str1.length) - 1))
				intermediateWordBuffer.append("~:");
			count++;
		}
		if (iterator.hasNext()) {
			String number = iterator.next();
			tempList = bucketedWords.get(number);
			sb = getStringBufferFromList(tempList);
			intermediateWordBuffer = findWords(intermediateWordBuffer, sb,
					iterator, bucketedWords);
		} else {
			return intermediateWordBuffer;
		}
		return intermediateWordBuffer;

	}

	/**
	 * findWords will find compbinations for every String, and will separate
	 * each word by ~:
	 * 
	 * 
	 * @param string
	 * @param str2
	 * @return
	 */
	private static String calculateCombinations(String string, String[] str2) {

		StringBuffer sb = new StringBuffer();
		int count = 0;
		for (String string2 : str2) {
			sb.append(string + "-" + string2);
			if (count < (str2.length) - 1)
				sb.append("~:");
			count++;
		}
		return sb.toString();
	}
}
