/**
 * 
 */
package com.aconex.factory;

import com.aconex.inputtypes.FileInput;
import com.aconex.inputtypes.NumberType;
import com.aconex.inputtypes.StringInput;

/**
 * InputFactory is a factory class which generates FileInput or StringInput
 * object based on  what user gives in command line argument. 
 * 
 * @author user
 *
 */
public abstract class InputFactory implements Input {

	public static Input getInput(String input) {
		if ((input.equals(NumberType.FILE_INPUT_PHONE.toString()))
				|| (input.equals(NumberType.FILE_INPUT_DICTIONARY.toString()))) {
			return new FileInput();
		} else {
			return new StringInput();
		}
	}

}
