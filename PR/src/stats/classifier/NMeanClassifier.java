package stats.classifier;

import stats.classifier.Classifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import Jama.Matrix;

public class NMeanClassifier extends Classifier {

	Map<Integer, Matrix> means;
	boolean useMachal;
	
	public NMeanClassifier() {

	}
	
	public NMeanClassifier(boolean useMachal) {
		this.useMachal = useMachal;
	}

	@Override
	protected DataSet objectsFromFeatures(double[][] Dataset, int[] classLabels) {
		int[][] featuresToWrite = new int[2][Dataset.length];
		for (int i = 0; i < Dataset.length; i++) {
			for (int objNum = 0; objNum < Dataset[i].length; objNum++) {
				if (Dataset[i][objNum] != 0.0) {
					featuresToWrite[classLabels[objNum]][i] = 1;
				}
			}
		}
		
		int[] featuresToWriteMerged = new int[Dataset.length];
		Arrays.fill(featuresToWriteMerged, 1);
		for (int featNum = 0; featNum < featuresToWrite[0].length; featNum++) {
			if (featuresToWrite[0][featNum] == 0 || featuresToWrite[1][featNum] == 0) {
				featuresToWriteMerged[featNum] = 0;
			}
		}

		int featuresToWriteCount = 0;
		for (int i = 0; i < featuresToWriteMerged.length; i++) {
			featuresToWriteCount += featuresToWriteMerged[i];
		}
		
		DataSet mainDataSet = new DataSet();
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
		System.out.println("fet: " + featuresToWriteCount);

		return mainDataSet;
	}
	
	@Override
	public void trainClassifier(double[][] Dataset, String TrainSetSize, int[] classLabels) {
		this.generateTrainingAndTestSets(Dataset, TrainSetSize, classLabels);
		
		Map<Integer, ArrayList<ClassificationObject>> classifObjsForClass = this.trainingObjects.getClassifObjectsForClass();
		this.means = this.trainingObjects.calculateFeatureMeansForClass(classifObjsForClass);
	}

	@Override
	public double testClassifier() {
		double goodClassifications = 0.0;

		Map<Integer, Matrix> covs = null;
		if (useMachal) {
			covs = this.trainingObjects.calculateCovarianceMatrixes();
		}
		
		for (ClassificationObject obj : testObjects.objects) {
			Double distance = null;
			for (Integer meanClass : this.means.keySet()) {
				
				if (useMachal) {//machal distance
					Matrix meanAsVector = this.means.get(meanClass);
					
					Matrix diff = obj.featuresToVector().minus(meanAsVector);
					Matrix cov = covs.get(meanClass);
					Matrix resultMatrix = (diff.transpose()).times(cov.inverse()).times(diff);

					Double machalTmp = resultMatrix.get(0, 0);
					machalTmp = Math.sqrt(machalTmp);

					if (distance == null || machalTmp.compareTo(distance) < 0) {
						obj.classAfterClassification = meanClass;
						distance = machalTmp;
					}
					
				} else {//euclidean distance
					Matrix mean = this.means.get(meanClass).transpose();
					
					Double distanceTmp = Classifier.distance(obj.features, mean.getArray()[0]);
					if (distance == null || distanceTmp.compareTo(distance) < 0) {
						distance = distanceTmp;
						obj.classAfterClassification = meanClass;
					}
				}
			}
			
			if (obj.classLabel == obj.classAfterClassification) {
				goodClassifications++;
			}
		}

		return goodClassifications / testObjects.objects.size();
	}

}
