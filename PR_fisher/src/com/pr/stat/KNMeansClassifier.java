package com.pr.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;

public class KNMeansClassifier extends Classifier {

	private static final double EPSILON = 0.01;

	List<MeanObject> means;
	boolean useMachal;

	public KNMeansClassifier() {

	}

	public KNMeansClassifier(boolean useMachal) {
		this.useMachal = useMachal;
	}

	@Override
	public void trainClassifier(double[][] dataset, String trainSetSize, int[] classLabels) {
		this.generateTrainingAndTestSets(dataset, trainSetSize, classLabels);
		try {
			this.doKMeans();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected DataSet objectsFromFeatures(double[][] Dataset, int[] classLabels) {
		DataSet mainDataSet = new DataSet();

		if (useMachal) {
			// count class types
			Set<Integer> classTypes = new HashSet<>();
			for (int i = 0; i < classLabels.length; i++) {
				classTypes.add(classLabels[i]);
			}

			int[][] featuresToWrite = new int[classTypes.size()][Dataset.length];
			for (int i = 0; i < Dataset.length; i++) {
				for (int objNum = 0; objNum < Dataset[i].length; objNum++) {
					if (Dataset[i][objNum] != 0.0) {
						featuresToWrite[classLabels[objNum]][i] = 1;
					}
				}
			}

			// features to write
			int[] featuresToWriteMerged = new int[Dataset.length];
			Arrays.fill(featuresToWriteMerged, 1);
			for (int featNum = 0; featNum < featuresToWrite[0].length; featNum++) {
				int toWrite = 1;
				for (int i = 0; i < classTypes.size(); i++) {
					if (featuresToWrite[i][featNum] == 0) {
						toWrite = 0;
					}
				}
				featuresToWriteMerged[featNum] = toWrite;
			}

			int featuresToWriteCount = 0;
			for (int i = 0; i < featuresToWriteMerged.length; i++) {
				featuresToWriteCount += featuresToWriteMerged[i];
			}

			// Create objects and set their class
			for (int i = 0; i < classLabels.length; i++) {
				mainDataSet.addClassificationObject(new ClassificationObject(classLabels[i], featuresToWriteCount));
			}

			int index = 0;
			for (int i = 0; i < Dataset.length; i++) {
				if (featuresToWriteMerged[i] == 1) {
					for (int k = 0; k < Dataset[i].length; k++) {
						mainDataSet.objects.get(k).features[index] = Dataset[i][k];
					}

					index++;
				}
			}

			System.out.println("Features to write: " + Arrays.toString(featuresToWriteMerged));
		} else {
			mainDataSet = super.objectsFromFeatures(Dataset, classLabels);
		}
		return mainDataSet;
	}

	@Override
	public double testClassifier() {
		double goodClassifications = 0.0;

		Map<Integer, Matrix> covs = null;
		if (useMachal) {
			covs = this.trainingObjects.calculateCovarianceMatrixes();
			/*
			 * for (Matrix cov : covs.values()) { cov.print(new DecimalFormat(),
			 * 9); }
			 */
		}

		for (ClassificationObject classifObj : this.testObjects.objects) {

			Double distance = null;
			for (MeanObject calculatedMean : this.means) {

				if (calculatedMean.classLabel != -1) {
					if (useMachal) {// machal distance
						Matrix diff = classifObj.featuresToVector().minus(calculatedMean.featuresToVector());
						Matrix cov = covs.get(new Integer(calculatedMean.classLabel));

						Matrix resultMatrix = (diff.transpose()).times(cov.inverse()).times(diff);

						Double machalTmp = resultMatrix.get(0, 0);
						machalTmp = Math.sqrt(machalTmp);

						if (distance == null || machalTmp.compareTo(distance) < 0) {
							classifObj.classAfterClassification = calculatedMean.classLabel;
							distance = machalTmp;
						}

					} else { // Euclidean distance
						Double distanceTmp = Classifier.distance(classifObj.features, calculatedMean.features);

						if (distance == null || distanceTmp.compareTo(distance) < 0) {
							classifObj.classAfterClassification = calculatedMean.classLabel;
							distance = distanceTmp;
						}
					}
				}
			}

			if (classifObj.classLabel == classifObj.classAfterClassification) {
				goodClassifications++;
			}
		}

		double classificationAccuracy = goodClassifications / this.testObjects.objects.size();

		return classificationAccuracy;
	}

	private void doKMeans() throws Exception {
		if (trainingObjects.objects.size() == 0) {
			throw new Exception("there are no training objects");
		}

		Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass = this.trainingObjects.getClassifObjectsForClass();
		List<MeanObject> meansForClass = new ArrayList<>();

		for (Integer classifClass : classifObjsForClass.keySet()) {
			int k = 1;
			Double lastTotalError = null;

			List<MeanObject> meansForClassTmp = null;
			while (true) {
				meansForClassTmp = this.calculateMeans(k, classifObjsForClass.get(classifClass));

				Double currTotalError = 0.0;
				for (MeanObject meanObj : meansForClassTmp) {
					currTotalError += meanObj.quantizationError;
				}

				currTotalError /= meansForClassTmp.size();
				if (lastTotalError != null && currTotalError.compareTo(lastTotalError) >= 0) {
					break;
				} else {
					lastTotalError = currTotalError;
					k++;
				}

				System.out.println("Quantization error: [current = " + currTotalError + "] [last = " + lastTotalError + "] [k = " + k + "]");
			}

			meansForClass.addAll(meansForClassTmp);
			System.out.println("For class: " + classifClass + " the adapted k = " + k + "\n");
		}

		this.means = meansForClass;
	}

	private List<MeanObject> calculateMeans(int currentK, List<ClassificationObject> objs) throws IOException {
		boolean meansChanging = true;

		// prepare random centroids
		List<MeanObject> means = new ArrayList<>(currentK);
		for (int i = 0; i < currentK; i++) {
			means.add(new MeanObject(-1, trainingObjects.getFeaturesNumber(), trainingObjects.minValue, trainingObjects.maxValue));
		}

		int currIteration = 0;
		while (meansChanging) {
			meansChanging = false;

			// for each training object find the nearest mean(centroid)
			for (ClassificationObject trainObj : objs) {
				Double dist = null;
				MeanObject currMean = null;
				for (MeanObject mean : means) {
					Double currDist = Classifier.distanceSquared(trainObj.features, mean.features);

					if (dist == null || currDist.compareTo(dist) < 0) {
						dist = currDist;
						currMean = mean;
					}
				}

				currMean.nearestObjects.add(trainObj);
			}

			// check if means changed, in other words check the STOP condition
			// for this algorithm
			for (MeanObject mean : means) {
				int representingClass = this.findClass(mean.nearestObjects);
				mean.classLabel = representingClass;

				// calculate mean based on the nearest objects
				double changeThreshold = mean.calculate();
				if (changeThreshold > EPSILON) {
					meansChanging = true;
				}
			}
			currIteration++;
		}

		System.out.println("Means calculation for [k = " + currentK + "] finished after [" + currIteration + "] iterations");

		return means;
	}
}
