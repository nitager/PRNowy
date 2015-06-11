package com.pr.stat;

import java.util.Arrays;

import Jama.Matrix;

public class ClassificationObject implements Comparable<ClassificationObject> {
	public double[] features;
	public int classLabel;
	public int classAfterClassification;
	public double distance;

	public ClassificationObject(int classLabel, int featuresSize) {
		this.classLabel = classLabel;
		this.features = new double[featuresSize];
	}

	@Override
	public int compareTo(ClassificationObject arg0) {
		return Double.compare(this.distance, arg0.distance);
	}

	public Matrix featuresToVector() {
		Matrix m = new Matrix(this.features, 1).transpose();
		return m;
	}

	public String getFeaturesAsString(String separator) {
		String result = "";
		for (int i = 0; i < this.features.length; i++) {
			result += this.features[i];

			if (i < this.features.length - 1) {
				result += separator;
			}
		}

		return result;
	}

	public double[] getMinMaxFeatureValue() {
		if (this.features != null && this.features.length > 0) {
			double[] featuresCopy = new double[this.features.length];
			System.arraycopy(this.features, 0, featuresCopy, 0, featuresCopy.length);
			Arrays.sort(featuresCopy);
			
			return new double[] { featuresCopy[0], featuresCopy[featuresCopy.length - 1] };
		}
		
		return null;
	}

	@Override
	public String toString() {
		return "ClassificationObject [classLabel=" + classLabel + ", " + "features" + Arrays.toString(features) + "]";
	}
}
