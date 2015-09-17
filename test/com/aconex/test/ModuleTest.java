package com.aconex.test;

import static org.junit.Assert.assertEquals;

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
import java.util.Scanner;

import org.junit.Test;

import com.aconex.common.Util;
import com.aconex.dictionary.Dictionary;
import com.aconex.dictionary.DictionaryImpl;
import com.aconex.factory.RecordsFactory;
import com.aconex.phonenumbers.PhoneNumber;
import com.aconex.phonenumbers.PhoneNumberImpl;

public class ModuleTest {

	List<String> phoneNumbers = new ArrayList<String>();
	List<String> dictionaryWords = new ArrayList<String>();

	private List<String> loadDictionaryWords(String dictionaryLoc)
			throws IOException {

		Enumeration<URL> en = getClass().getClassLoader().getResources(
				dictionaryLoc.trim());
		if (en.hasMoreElements()) {
			URL metaInf = en.nextElement();
			try (BufferedReader br = new BufferedReader(new FileReader(
					new File(metaInf.toURI())))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					if (line.length() > 0)
						dictionaryWords.add(line.trim());
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dictionaryWords;

	}

	private List<String> loadPhoneNumbers(String phLoc) throws IOException {

		Enumeration<URL> en = getClass().getClassLoader().getResources(
				phLoc.trim());

		if (en.hasMoreElements()) {
			URL metaInf = en.nextElement();
			try (BufferedReader br = new BufferedReader(new FileReader(
					new File(metaInf.toURI())))) {
				String line = "";
				while ((line = br.readLine()) != null) {
					if (line.length() > 0)
						phoneNumbers.add(line.trim());
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return phoneNumbers;

	}

	/**
	 * This method loads phone numbers and dictionary from files. File locations
	 * is entered by user.Then result is processed and words are randomly tested
	 * for match
	 * 
	 * @throws IOException
	 */
	@Test
	public void getPossibleOutputsTest() throws IOException {
		List<String> list = new ArrayList<String>();
		List<String> bucketedWords = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		String dictionaryLoc = "";
		String phLoc = "";
		System.out.println("Enter phone file location");
		phLoc = sc.nextLine();
		list = loadPhoneNumbers(phLoc);
		System.out.println("Enter dictionay file location");
		sc = new Scanner(System.in);
		dictionaryLoc = sc.nextLine();
		// int input = s.nextInt(); if you want integers
		bucketedWords = loadDictionaryWords(dictionaryLoc);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map = Util.populatedBucketedWords(bucketedWords);
		PhoneNumber phNumber = null;
		Dictionary dic = null;
		dic = (DictionaryImpl) RecordsFactory.getInstance(Util.DICTIONARY);
		dic.loadRecords(bucketedWords);// list of PhoneNumbers
		dic.setBucketsOfPhNoWords(map);
		// {222=[aaa, bbb, ccc], 2255=[call, calk], 222333=[aaaeee],
		// 333333=[dddeee], 63=[me]}
		phNumber = (PhoneNumberImpl) RecordsFactory.getInstance(Util.PHONE);
		phNumber.loadRecords(list);
		HashMap<String, StringBuffer> resultsMap = phNumber
				.getPossibleOutputs(dic);
		System.out.println(resultsMap);
		String expected = (resultsMap.get("225563")).toString();
		assertEquals(expected, "call-me~:calk-me");
		expected = (resultsMap.get("333333")).toString();
		assertEquals(expected, "defdee");

	}

	/*
	 * This method loads phone numbers and dictionary .Values are entered
	 * manually
	 */
	@Test
	public void getPossibleOutputsTest2() {
		List<String> list = new ArrayList<String>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		list.add("222");
		list.add("222333");
		List<String> bucketedWords = new ArrayList<String>();
		PhoneNumber phNumber = null;
		bucketedWords.add("aaa");
		bucketedWords.add("bbb");
		bucketedWords.add("ccc");
		map.put("222", bucketedWords);
		bucketedWords = new ArrayList<String>();
		bucketedWords.add("call");
		bucketedWords.add("calk");
		bucketedWords.add("ccc");
		map.put("2255", bucketedWords);
		bucketedWords = new ArrayList<String>();
		bucketedWords.add("aaaeee");
		map.put("222333", bucketedWords);
		bucketedWords = new ArrayList<String>();
		bucketedWords.add("dddeee");
		map.put("333333", bucketedWords);
		bucketedWords = new ArrayList<String>();
		bucketedWords.add("me");
		map.put("63", bucketedWords);
		Dictionary dic = null;
		dic = (DictionaryImpl) RecordsFactory.getInstance(Util.DICTIONARY);
		// dic.loadRecords(list);//list of PhoneNumbers
		dic.setBucketsOfPhNoWords(map);
		phNumber = (PhoneNumberImpl) RecordsFactory.getInstance(Util.PHONE);
		phNumber.loadRecords(list);
		HashMap<String, StringBuffer> resultsMap = phNumber
				.getPossibleOutputs(dic);
		System.out.println(resultsMap);
		assertEquals(resultsMap.toString(),
				"{222=aaa~:bbb~:ccc, 222333=aaaeee}");

	}
}
