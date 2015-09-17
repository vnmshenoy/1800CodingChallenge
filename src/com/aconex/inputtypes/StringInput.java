package com.aconex.inputtypes;

import java.util.ArrayList;
import java.util.List;

import com.aconex.factory.Input;

public class StringInput implements Input {
	/*
	 * * mergeInputData method reads every record in the input and adds it to a
	 * List mergedInputData.For eg, if input given in command line was like 111
	 * 222 then list would return mergedInputData<[111,222]..>
	 * 
	 * @see com.aconex.inputtypes.Input#mergeInputData(java.util.List)
	 */
	@Override
	public List<String> mergeInputData(List<String> s) {
		List<String> listOfPhoneNumbers = new ArrayList<String>();
		for (String phoneNumber : s) {
			phoneNumber = phoneNumber.replaceAll("\\W", "");
			listOfPhoneNumbers.add(phoneNumber.trim());
		}
		return listOfPhoneNumbers;
	}

}
