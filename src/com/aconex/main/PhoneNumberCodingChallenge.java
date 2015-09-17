/**
 * 
 */
package com.aconex.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aconex.common.Util;
import com.aconex.dictionary.Dictionary;
import com.aconex.dictionary.DictionaryImpl;
import com.aconex.factory.Input;
import com.aconex.factory.InputFactory;
import com.aconex.factory.Records;
import com.aconex.factory.RecordsFactory;
import com.aconex.inputtypes.NumberType;
import com.aconex.phonenumbers.PhoneNumber;
import com.aconex.phonenumbers.PhoneNumberImpl;

/**
 * This is the entry point of the application.The goal of the application is to
 * allow an end user to see all possible outputs for a particular phone number
 * 
 * @author user
 *
 */
public class PhoneNumberCodingChallenge {

	/**
	 * Main method takes arguments from end user.End user can provide a list of
	 * files of phone numbers or from STDIN.User also has the option to set his
	 * own dictionary if needed.However, if no dictionary location is provided,
	 * default dictionary file is used.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// input map is populated using readArgument method of Util class.It
		// will value like
		// key=FILE_INPUT,value=<file1.txt,file2.txt>
		Map<String, List<String>> inputMap = Util.readArguments(args);
		List<String> listOfFiles = null;
		List<String> listOfStringInputs = null;
		List<String> listOfRecords = null;
		List<String> listOfDictionaryWords = null;
		String fileInput = NumberType.FILE_INPUT_PHONE.toString();
		String stringInput = NumberType.STRING_INPUT.toString();
		listOfFiles = inputMap.get(fileInput);
		Records record = null;
		PhoneNumber phNumber = null;
		Dictionary dic = null;
		listOfStringInputs = inputMap.get(stringInput);
		// phInput will be created of Type "FileInput" or "StringInput" type
		Input phInput = null;
		Input dicInput = null;
		if (listOfFiles != null && listOfFiles.size() > 0) {
			phInput = InputFactory.getInput(fileInput);
			// listOfPhoneNumbers is list which will have a list of records.Eg
			// listOfPhoneNumbers <[111,222]..>
			listOfRecords = phInput.mergeInputData(listOfFiles);
		} else if (listOfStringInputs != null && listOfStringInputs.size() > 0) {
			phInput = InputFactory.getInput(stringInput);
			listOfRecords = phInput.mergeInputData(listOfStringInputs);
		}
		phNumber = (PhoneNumberImpl) RecordsFactory.getInstance(Util.PHONE);
		phNumber.loadRecords(listOfRecords);
		dicInput = InputFactory.getInput(fileInput);
		listOfFiles = inputMap.get(NumberType.FILE_INPUT_DICTIONARY.toString());
		if (listOfFiles != null && listOfFiles.size() > 0) {
			listOfRecords = dicInput.mergeInputData(listOfFiles);
			dic = (DictionaryImpl) RecordsFactory.getInstance(Util.DICTIONARY);
			dic.loadRecords(listOfRecords);
		}
		if (dic != null) {
			dic.populateBucketsOfPhNoWords();
		} 
		HashMap<String, StringBuffer> mappedPhToWords = phNumber
				.getPossibleOutputs(dic);
		PhoneNumberImpl phResults = (PhoneNumberImpl) phNumber;
		phResults.displayOutput(mappedPhToWords);
	}
}
