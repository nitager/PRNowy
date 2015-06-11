package com.pr.stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KNNeighbourClassifier extends Classifier {
	int k;

	public KNNeighbourClassifier(int k) {
		this.k = k;
	}

	@Override
	public void trainClassifier(double[][] Dataset, String TrainSetSize, int[] classLabels) {
		this.generateTrainingAndTestSets(Dataset, TrainSetSize, classLabels);
	}

	@Override
	public double testClassifier() {
		double goodClassifications = 0.0;
		
		for (ClassificationObject obj : this.testObjects.objects) {
			//important - find k nearest based on training objects
			ArrayList<ClassificationObject> nearest = this.findKNearests(this.trainingObjects.objects, obj, k);
			obj.classAfterClassification = findClass(nearest);

			if (obj.classLabel == obj.classAfterClassification) {
				goodClassifications++;
			}

		}

		return goodClassifications / testObjects.objects.size();
	}
	
	/**
	 * Finding k- neares centroids to classifiedObject from trainingObjects
	 * according to Eucidlean distance
	 * 
	 * @param trainingObjects
	 * @param classifiedObj
	 * @param k
	 * @return k nearest centroids
	 */
	private ArrayList<ClassificationObject> findKNearests(List<? extends ClassificationObject> objs, ClassificationObject classifiedObj, int k) {

		ArrayList<ClassificationObject> nearests = new ArrayList<>();

		for (ClassificationObject obj : objs) {
			obj.distance = Classifier.distance(obj.features, classifiedObj.features);
		}

		Collections.sort(objs);

		for (int i = 0; i < k; i++) {
			nearests.add(objs.get(i));
		}

		return nearests;
	}
}
