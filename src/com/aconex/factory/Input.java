package com.aconex.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Input {

	List<String> mergeInputData(List<String> s) throws FileNotFoundException,
			IOException;
}
