/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


import stats.classifier.ClassificationObject;
import stats.classifier.ClassificationObject;
/**
 *
 * @author Piotr
 */
public class FileUtility {

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
