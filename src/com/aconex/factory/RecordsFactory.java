package com.aconex.factory;

import com.aconex.common.Util;
import com.aconex.dictionary.DictionaryImpl;
import com.aconex.phonenumbers.PhoneNumberImpl;

/**
 * RecordsFactory is a factory class which generates "PhoneNumber" object or
 * "Dictionary" object based on the input.
 * 
 * @author user
 *
 */
public class RecordsFactory {

	/**
	 * getInstance is a factory method which returns Concrete Phone or
	 * Dictionary Object based on input
	 * 
	 * @param input
	 * @return
	 */
	public static Records getInstance(String input) {

		if (input.equals(Util.PHONE)) {
			return new PhoneNumberImpl();
		} else {
			return new DictionaryImpl();
		}
	}

}
