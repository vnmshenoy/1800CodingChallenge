package com.aconex.phonenumbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aconex.dictionary.Dictionary;
import com.aconex.factory.Records;

/**
 * PhoneNumber is an abstract class which has methods to loadRecords i.e
 * listOfRecods into memory.listOfRecods will contain all phone numbers read
 * from file.
 * 
 * @author user
 *
 */
public abstract class PhoneNumber implements Records {

	private List<String> listOfRecods = new ArrayList<String>();

	public abstract HashMap<String, StringBuffer> getPossibleOutputs(
			Dictionary dic);

	@Override
	public void loadRecords(List<String> list) {
		this.listOfRecods = list;
	}

	public List<String> getListOfRecods() {
		return listOfRecods;
	}

	public void setListOfRecods(List<String> listOfRecods) {
		this.listOfRecods = listOfRecods;
	}
}
