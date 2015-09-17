package com.aconex.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.aconex.factory.Input;
import com.aconex.factory.InputFactory;
import com.aconex.inputtypes.NumberType;

/**
 * This class basically tests whether files get properly loaded in system
 * 
 * @author user
 *
 */
public class FileInputTest {
	List<String> phoneNumbers = new ArrayList<String>();
	List<String> dictionaryWords = new ArrayList<String>();
	List<String> listOfFiles = null;
	List<String> list = null;
	String[] str = null;	
	Map<String, List<String>> inputMap = new HashMap<String, List<String>>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void loadDictionaryWords() throws Exception {
		String fileInputDic = NumberType.FILE_INPUT_DICTIONARY.toString();
		list = new ArrayList<String>();
		// dictinoaryFile=SampleDic1.txt;phFile=samplePhoneNumbers.txt
		String commandLineArg = System.getProperty("dictionaryFile");
		String dictionary = "";
		if (commandLineArg!=null&&commandLineArg.contains(";")) {
			str = commandLineArg.split(";");
			dictionary = str[0];
		} else {
			dictionary = commandLineArg;
		}
		list.add(dictionary); //
		inputMap.put("FILE_INPUT_DICTIONARY", list);
		listOfFiles = inputMap.get(fileInputDic);
		Input dicInput = InputFactory.getInput(fileInputDic);
		dictionaryWords = dicInput.mergeInputData(listOfFiles);	
	
	}

	@Before
	public void loadPhoneNumbers() throws Exception {
		String fileInputPhone = NumberType.FILE_INPUT_PHONE.toString();
		list = new ArrayList<String>();
		// dictinoaryFile=SampleDic1.txt;phFile=samplePhoneNumbers.txt
		String phoneNumber = System.getProperty("phFile");
		list.add(phoneNumber); //
		inputMap.put("FILE_INPUT_PHONE", list);
		listOfFiles = inputMap.get(fileInputPhone);
		Input phInput = InputFactory.getInput(fileInputPhone);
		phoneNumbers= phInput.mergeInputData(listOfFiles);// listOfFiles
																	// can be
																// [example.txt]
	}
	


	@After
	public void tearDown() throws Exception {
	}

	/*
	 * @Test public void testSizeOfPhoneNumberInput() { int
	 * size=phoneNumbers.size(); assertEquals(size,5); }
	 */

	@Test
	public void testSizeOfDictionaryInput() {
		int size = dictionaryWords.size();
		assertEquals(size, 22983);
	}

	@Test
	public void testSizeOfDictionaryInputNegative() {
		int size = dictionaryWords.size();
		assertNotSame(size, 7);
	}

	@Test
	public void testSizeOfPhoneNumbers() {
		int size = phoneNumbers.size();
		assertEquals(size, 30);
	}

}
