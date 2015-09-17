package com.aconex.inputtypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.aconex.factory.Input;

public class FileInput implements Input {

	/*
	 * mergeInputData  method reads every record in File and adds
	 * it to a List mergedInputData.For eg, if file had records like 111 222
	 * then list would return mergedInputData<[111,222]..> (non-Javadoc)
	 * 
	 * @see com.aconex.inputtypes.Input#mergeInputData(java.util.List)
	 */
	@Override
	public List<String> mergeInputData(List<String> s) throws IOException {

		List<String> mergedInputData = new ArrayList<String>();
		for (String string : s) {
			Enumeration<URL> en = getClass().getClassLoader().getResources(
					string);
			if (en.hasMoreElements()) {
				URL metaInf = en.nextElement();
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(metaInf.openStream()))) {
					String line = "";
					while ((line = br.readLine()) != null) {
						if (line.length() > 0)
							line = line.replaceAll("\\W", "");
							mergedInputData.add(line.trim());
					}
				}

			}
		}

		return mergedInputData;
	}
}
