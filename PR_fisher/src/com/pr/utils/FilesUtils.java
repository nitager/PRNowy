package com.pr.utils;

import com.pr.stat.ClassificationObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FilesUtils {

	public static final String SEPARATOR = ";";
	
	public static void saveToFile(List<? extends ClassificationObject> classificationObjects, String filePath) throws IOException {
		saveToFile(classificationObjects, SEPARATOR, filePath);
	}

	public static <TYPE extends ClassificationObject> void saveToFile(TYPE[] classificationObjects, String filePath) throws IOException {
		saveToFile(classificationObjects, SEPARATOR, filePath);
	}
	
	public static <TYPE extends ClassificationObject> void saveToFile(List<TYPE> classificationObjects, String separator, String filePath) throws IOException {
		saveToFile(classificationObjects.toArray(new ClassificationObject[0]), separator, filePath);
	}
	
	public static <TYPE extends ClassificationObject> void saveToFile(TYPE[] classificationObjects, String separator, String filePath) throws IOException {
		File resultFile = new File(filePath);
		if (resultFile.getParentFile() != null) {
			resultFile.getParentFile().mkdirs();
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
			for (ClassificationObject classifObj : classificationObjects) {
				writer.write(classifObj.getFeaturesAsString(separator) + "\n");
			}
		}
	}
}
