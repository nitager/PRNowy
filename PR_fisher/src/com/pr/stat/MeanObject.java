package classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MeanObject extends ClassificationObject {

	ArrayList<ClassificationObject> nearestObjects = new ArrayList<>();
	Double quantizationError;

	public MeanObject(int classLabel, int featuresSize) {
		super(classLabel, featuresSize);
	}

	public MeanObject(int classLabel, int featuresSize, double min, double max) {
		super(classLabel, featuresSize);

		this.randomFeatures(min, max);
	}

	public double calculate() {
		double changeThreshold = 0.0;
		this.quantizationError = 0.0;
		
		if (!this.nearestObjects.isEmpty()) {
			double[] newFeatures = new double[this.features.length];
			Arrays.fill(newFeatures, 0);

			// summarize appropriate features
			for (ClassificationObject nearestObject : this.nearestObjects) {
				for (int featNum = 0; featNum < nearestObject.features.length; featNum++) {
					newFeatures[featNum] += nearestObject.features[featNum];
				}
			}

			// calculate mean
			for (int featNum = 0; featNum < newFeatures.length; featNum++) {
				newFeatures[featNum] /= this.nearestObjects.size();
			}

			// calculate change
			changeThreshold = Classifier.distance(this.features, newFeatures);

			this.features = newFeatures;
			
			for (ClassificationObject nearestObject : this.nearestObjects) {
				quantizationError += Classifier.distance(nearestObject.features, this.features);
			}

			this.nearestObjects = new ArrayList<>();
		}
		
		return changeThreshold;
	}

	private void randomFeatures(double min, double max) {
		Random rand = new Random();
		for (int i = 0; i < this.features.length; i++) {
			this.features[i] = rand.nextDouble() * max - min;
		}
	}
}
