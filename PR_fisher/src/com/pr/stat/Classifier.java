package classifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class Classifier {

	DataSet trainingObjects = new DataSet();
	DataSet testObjects = new DataSet();

	protected void generateTrainingAndTestSets(double[][] Dataset, String TrainSetSize, int[] classLabels) {
		this.trainingObjects.clear();
		this.testObjects.clear();
		
		DataSet mainDataSet = objectsFromFeatures(Dataset, classLabels);
		
		Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass = mainDataSet.getClassifObjectsForClass();

		double percent = Double.parseDouble(TrainSetSize) / 100.0;
		for (Integer classLabel : classifObjsForClass.keySet()) {
			ArrayList<ClassificationObject> objs = classifObjsForClass.get(classLabel);
			double trainingPart = objs.size() * percent;
			for (int i = 0; i < objs.size(); i++) {
				if (i <= trainingPart) {
					trainingObjects.addClassificationObject(objs.get(i));
				} else {
					testObjects.addClassificationObject(objs.get(i));
				}
			}
		}

		System.out.println("\n\nTraining set size: [" + this.trainingObjects.objects.size() + "] [MIN: " + this.trainingObjects.minValue + "] [MAX: " + this.trainingObjects.maxValue + "]");
		System.out.println("Testing set size: [" + this.testObjects.objects.size() + "] [MIN: " + this.testObjects.minValue + "] [MAX: " + this.testObjects.maxValue + "]");
	}

	/**
	 * Makes list of {@link ClassificationObject} from [][]array of features.
	 * 
	 * @param Dataset
	 * @param classLabels
	 * @return
	 */
	protected DataSet objectsFromFeatures(double[][] Dataset, int[] classLabels) {
		DataSet mainDataSet = new DataSet();
		// Create objects and set their class
		for (int i = 0; i < classLabels.length; i++) {
			mainDataSet.addClassificationObject(new ClassificationObject(classLabels[i], Dataset.length));
		}

		for (int i = 0; i < Dataset.length; i++) {
			for (int k = 0; k < Dataset[i].length; k++) {
				mainDataSet.objects.get(k).features[i] = Dataset[i][k];
			}
		}

		return mainDataSet;
	}

	/**
	 * Calculates which class is most represented i nearest centroids
	 * 
	 * @param nearest
	 *            List of nearest centroids
	 * @return ClassLabel of most represented class
	 */
	protected int findClass(List<ClassificationObject> nearest) {
		if (nearest != null && !nearest.isEmpty()) {
			HashSet<ClassOccurences> classes = new HashSet<>();

			for (ClassificationObject neighbour : nearest) {
				classes.add(new ClassOccurences(neighbour.classLabel));
			}

			for (ClassOccurences occurence : classes) {
				for (ClassificationObject neighbour : nearest) {
					if (occurence.classLabel == neighbour.classLabel) {
						occurence.occurences++;
					}
				}
			}

			return Collections.max(classes).classLabel;
		}

		return -1;
	}

	abstract public void trainClassifier(double[][] Dataset, String TrainSetSize, int[] classLabels);

	abstract public double testClassifier();

	/**
	 * Squared euclidean distance between two points. Points must have same
	 * size.
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double distanceSquared(double[] point1, double[] point2) {
		double result = 0;

		for (int i = 0; i < point1.length; i++) {
			result += (point1[i] - point2[i]) * (point1[i] - point2[i]);
		}

		return result;
	}

	/**
	 * Euclidean distance between two points. Points must have same size.
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double distance(double[] point1, double[] point2) {
		double result = distanceSquared(point1, point2);
		result = Math.sqrt(result);
		return result;

	}
}
