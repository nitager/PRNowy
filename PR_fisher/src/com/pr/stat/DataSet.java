package classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;

public class DataSet {

	ArrayList<ClassificationObject> objects = new ArrayList<>();
	Double minValue;
	Double maxValue;

	public void addClassificationObject(ClassificationObject classificationObject) {
		this.objects.add(classificationObject);

		// calculate min and max values
		double[] currObjMinMax = classificationObject.getMinMaxFeatureValue();
		if (minValue == null || (minValue.doubleValue() > currObjMinMax[0])) {
			minValue = currObjMinMax[0];
		}

		if (maxValue == null || (maxValue.doubleValue() < currObjMinMax[1])) {
			maxValue = currObjMinMax[1];
		}
	}

	public void clear() {
		this.objects = new ArrayList<>();
		this.minValue = null;
		this.maxValue = null;
	}

	public int getFeaturesNumber() {
		if (!this.objects.isEmpty()) {
			return this.objects.get(0).features.length;
		}

		return 0;
	}

	public Map<Integer, Matrix> calculateCovarianceMatrixes() {
		Map<Integer, Matrix> matrixes = new HashMap<>();

		Map<Integer, Matrix> diffs = this.calculateDiffs();
		for (Integer classifClass : diffs.keySet()) {
			Matrix diff = diffs.get(classifClass); 
			Matrix transposed = diff.transpose();
			Matrix cov = diff.times(transposed).times(1.0/diff.getColumnDimension());
			
			matrixes.put(classifClass, cov);
		}
		
		return matrixes;
	}

	private Map<Integer, Matrix> calculateDiffs() {
		Map<Integer, Matrix> diffs = new HashMap<>();

		Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass = this.getClassifObjectsForClass();

		Map<Integer, Matrix> means = this.calculateFeatureMeansForClass(classifObjsForClass);

		for (Integer classifClass : classifObjsForClass.keySet()) {
			List<ClassificationObject> classifObjects = classifObjsForClass.get(classifClass);

			double[][] covTmp = new double[this.getFeaturesNumber()][classifObjects.size()];
			Matrix mean = means.get(classifClass);
			for (int objNum = 0; objNum < classifObjects.size(); objNum++) {
				ClassificationObject classifObj = classifObjects.get(objNum);
				for (int featNum = 0; featNum < classifObj.features.length; featNum++) {
					covTmp[featNum][objNum] = classifObj.features[featNum] - mean.get(featNum, 0);
				}
			}

			Matrix diff = new Matrix(covTmp);
			diffs.put(classifClass, diff);
		}

		return diffs;
	}

	public Map<Integer, Matrix> calculateFeatureMeansForClass(Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass) {
		Map<Integer, Matrix> featureMeansForClass = new HashMap<>();

		for (Integer classifClass : classifObjsForClass.keySet()) {

			List<ClassificationObject> classifObjects = classifObjsForClass.get(classifClass);

			// summarize features
			double[] meanTmp = new double[this.getFeaturesNumber()];
			Arrays.fill(meanTmp, 0.0);
			for (ClassificationObject classifObj : classifObjects) {
				for (int featureNum = 0; featureNum < classifObj.features.length; featureNum++) {
					meanTmp[featureNum] += classifObj.features[featureNum];
				}
			}

			// calculate mean
			for (int i = 0; i < meanTmp.length; i++) {
				meanTmp[i] /= classifObjects.size();
			}

			// convert mean to matrix and add its to the result map
			Matrix mean = new Matrix(meanTmp, 1).transpose();
			featureMeansForClass.put(classifClass, mean);
		}

		return featureMeansForClass;
	}

	/**
	 * Returns the Map containing the list of classification objects for each
	 * available classification class
	 */
	public Map<Integer, ArrayList<ClassificationObject>> getClassifObjectsForClass() {
		Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass = new HashMap<>();

		for (ClassificationObject classifObj : this.objects) {
			Integer currObjClassLabel = new Integer(classifObj.classLabel);
			if (!classifObjsForClass.containsKey(currObjClassLabel)) {
				classifObjsForClass.put(currObjClassLabel, new ArrayList<ClassificationObject>());
			}

			ArrayList<ClassificationObject> currClassObjects = classifObjsForClass.get(currObjClassLabel);
			currClassObjects.add(classifObj);
		}

		return classifObjsForClass;
	}
}
