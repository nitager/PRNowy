package com.pr.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Jama.Matrix;
import com.pr.stat.Classifier;
import statystyczne.FeaturesSelectionResult;

/**
 * @author krzy
 * @author Jakub Bentyn 194311
 * @author Kamil Chaber 194315
 * 
 */
public class PR_Logic {

	protected String inputData; 			  // dataset from a text file will be placed here
	protected int classCount = 0;
	protected int featureCount = 0;
	protected double[][] features;			  // original feature matrix
	protected double[][] featuresTransformed; // and transformed feature matrix
	protected int[] classLabels;
	protected int[] sampleCount;
	protected String[] classNames;
        
	
	public Classifier actualClassifier;

	public String readDataSet(PR_Window window) {
		String s_tmp, s_out = "";
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(".."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Datasets - plain text files", "txt");
		jfc.setFileFilter(filter);
		if (jfc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(jfc.getSelectedFile()));
				while ((s_tmp = br.readLine()) != null)
					s_out += s_tmp + '$';
				br.close();
				window.l_dataset_name.setText(jfc.getSelectedFile().getName());
			} catch (Exception e) {
			}
		}
                System.out.println("Odczytano zbór");
		return s_out;
                
	}

	public void getDatasetParameters() throws Exception {
		// based on data stored in InData determine: class count and names,
		// number of samples
		// and number of features; set the corresponding variables
		String stmp = inputData, saux = "";
		// analyze the first line and get feature count: assume that number of
		// features
		// equals number of commas
		saux = inputData.substring(inputData.indexOf(',') + 1, inputData.indexOf('$'));
		if (saux.length() == 0)
			throw new Exception("The first line is empty");
		// saux stores the first line beginning from the first comma
		int count = 0;
		while (saux.indexOf(',') > 0) {
			saux = saux.substring(saux.indexOf(',') + 1);
			count++;
		}
		featureCount = count + 1; // the first parameter
		// Determine number of classes, class names and number of samples per
		// class
		boolean New;
		int index = -1;
		List<String> NameList = new ArrayList<String>();
		List<Integer> CountList = new ArrayList<Integer>();
		List<Integer> LabelList = new ArrayList<Integer>();
		while (stmp.length() > 1) {
			saux = stmp.substring(0, stmp.indexOf(' '));
			New = true;
			index++; // new class index
			for (int i = 0; i < NameList.size(); i++)
				if (saux.equals(NameList.get(i))) {
					New = false;
					index = i; // class index
				}
			if (New) {
				NameList.add(saux);
				CountList.add(0);
			} else {
				CountList.set(index, CountList.get(index).intValue() + 1);
			}
			LabelList.add(index); // class index for current row
			stmp = stmp.substring(stmp.indexOf('$') + 1);
		}
		// based on results of the above analysis, create variables
		classNames = new String[NameList.size()];
		for (int i = 0; i < classNames.length; i++)
			classNames[i] = NameList.get(i);
		sampleCount = new int[CountList.size()];
		for (int i = 0; i < sampleCount.length; i++)
			sampleCount[i] = CountList.get(i).intValue() + 1;
		classLabels = new int[LabelList.size()];
		for (int i = 0; i < classLabels.length; i++)
			classLabels[i] = LabelList.get(i).intValue();
                
                
                classCount=classNames.length;
                System.out.println("Załadowano zbiór danych");
	}

	public void fillFeatureMatrix() throws Exception {
		// having determined array size and class labels, fills in the feature
		// matrix
		int n = 0;
		String saux, stmp = inputData;
		for (int i = 0; i < sampleCount.length; i++)
			n += sampleCount[i];
		if (n <= 0)
			throw new Exception("no samples found");
		features = new double[featureCount][n]; // samples are placed
		// column-wise
		for (int j = 0; j < n; j++) {
			saux = stmp.substring(0, stmp.indexOf('$'));
			saux = saux.substring(stmp.indexOf(',') + 1);
			for (int i = 0; i < featureCount - 1; i++) {
				features[i][j] = Double.parseDouble(saux.substring(0, saux.indexOf(',')));
				saux = saux.substring(saux.indexOf(',') + 1);
			}
			features[featureCount - 1][j] = Double.parseDouble(saux);
			stmp = stmp.substring(stmp.indexOf('$') + 1);
		}
	System.out.println("Załadowano macierz danych");
        }

	public FeaturesSelectionResult selectFeatures(int[] flags, int d, boolean traceVsDet) {
		// for now: check all individual features using 1D, 2-class Fisher criterion
		FeaturesSelectionResult result = new FeaturesSelectionResult();

		if (d == 1) {
			double tmp;
			for (int i = 0; i < featureCount; i++) {
				tmp = computeFisherLD(features[i]);
				if ( result.getFisherLD() == null || (result.getFisherLD() != null && tmp > result.getFisherLD().doubleValue()) ) {
					result.setFeature(i);
					result.setFisherLD(tmp);
				}
			}
			
		} 
		else {	
			result = this.calculateSFS(d, classNames, classLabels, features, traceVsDet);
			System.out.println("\n\nWINNER: \n" + result);
		}

		return result;
                
	}

	public void setTransformedFeatures (FeaturesSelectionResult results) {
		featuresTransformed = new double [results.getFeatures().size()][];
		for (int i = 0; i < results.getFeatures().size(); i++) {
			featuresTransformed[i] = features[results.getFeatures().get(i)].clone(); 
		}
	}
	
	public double computeFisherLD(double[] vec) {
		// 1D, 2-classes
		double mA = 0, mB = 0, sA = 0, sB = 0;
		for (int i = 0; i < vec.length; i++) {
			if (classLabels[i] == 0) {
				mA += vec[i];
				sA += vec[i] * vec[i];
			} else {
				mB += vec[i];
				sB += vec[i] * vec[i];
			}
		}
		mA /= sampleCount[0];
		mB /= sampleCount[1];
		sA = sA / sampleCount[0] - mA * mA;
		sB = sB / sampleCount[1] - mB * mB;
		return Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB));
	}
	
	public double computeMultiDimFisherLD(int[] selectedFeatures, String[] classNames, int[] classLabels, double[][] features, boolean traceVsDet) {
		System.out.println("\n\nFEATURES: ");
		System.out.println(Arrays.toString(selectedFeatures));
		
		Matrix[] means = this.calculateMeans(selectedFeatures, classNames, classLabels, features);
		
		Matrix[] samples = this.prepareSamples(selectedFeatures, classNames, classLabels, features);
		
		Matrix[] meansPopulated = this.populateMeans(means, sampleCount);	
		
		Matrix[] covs = this.calculateCovarianceMatrixes(samples, meansPopulated);
		/*System.out.println("covs: ");
		for (int i = 0; i < covs.length; i++) {
			System.out.println("Cov class: " + i);
			covs[i].print(new DecimalFormat(), 4);
		}*/
		
		Matrix meansDiff = means[0];
		for (int meanNum = 1; meanNum < means.length; meanNum++) {
			meansDiff = meansDiff.minus(means[meanNum]);
		}
		/*System.out.println("\nMEANS DIFF:");
		meansDiff.print(new DecimalFormat(), 8);*/

		//Fisher threshold nominator (the length of means difference vector)
		double nominator = 0;
		for (int row = 0; row < meansDiff.getRowDimension(); row++) {
			nominator += meansDiff.get(row, 0) * meansDiff.get(row, 0); 
		}
		nominator = Math.sqrt(nominator);
		System.out.println("nominator: " + nominator);
		
		//Fisher threshold denominator (the sum of covariance matrixes' determinants)
		double denominator = 0;
		for (int covNum = 0; covNum < covs.length; covNum++) {
			if (traceVsDet) {
				denominator += covs[covNum].trace();
			} else {
				denominator += covs[covNum].det();
			}
		}
		System.out.println("denominator: " + denominator);
		System.out.println("FLD: " + nominator/denominator);
		
		return nominator/denominator;
	}

	public Matrix extractFeatures(Matrix C, double Ek, int k) {

		Matrix evecs, evals;
		// compute eigenvalues and eigenvectors
		evecs = C.eig().getV();
		evals = C.eig().getD();

		// PM: projection matrix that will hold a set dominant eigenvectors
		Matrix PM;
		if (k > 0) {
			// preset dimension of new feature space
			// PM = new double[evecs.getRowDimension()][k];
			PM = evecs.getMatrix(0, evecs.getRowDimension() - 1, evecs.getColumnDimension() - k, evecs.getColumnDimension() - 1);
		} else {
			// dimension will be determined based on scatter energy
			double TotEVal = evals.trace(); // total energy
			double EAccum = 0;
			int m = evals.getColumnDimension() - 1;
			while (EAccum < Ek * TotEVal) {
				EAccum += evals.get(m, m);
				m--;
			}
			PM = evecs.getMatrix(0, evecs.getRowDimension() - 1, m + 1, evecs.getColumnDimension() - 1);
		}

		return PM;
	}

	public Matrix computeCovarianceMatrix(double[][] m) {
		Matrix M = new Matrix(m);
		Matrix MT = M.transpose();
		Matrix C = M.times(MT);

		return C;
	}

	public double[][] centerAroundMean(double[][] M) {

		double[] mean = new double[M.length];
		for (int i = 0; i < M.length; i++)
			for (int j = 0; j < M[0].length; j++)
				mean[i] += M[i][j];
		for (int i = 0; i < M.length; i++)
			mean[i] /= M[0].length;
		for (int i = 0; i < M.length; i++)
			for (int j = 0; j < M[0].length; j++)
				M[i][j] -= mean[i];
		return M;
	}

	public double[][] projectSamples(Matrix FOld, Matrix TransformMat) {

		return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
	}
	
	/**
	 * Calculates means for given features in each class
	 * @param featuresIndexes Feature numbers
	 * @param classNames Array with available class names
	 * @param features Read feature values
	 * @return
	 */
	private Matrix[] calculateMeans(int[] featuresIndexes, String[] classNames, int[] classLabels, double[][] features) {
		Matrix[] means = new Matrix[classNames.length];
		//initialize matrix array
		for (int classNum = 0; classNum < classNames.length; classNum++) {
			means[classNum] = new Matrix(featuresIndexes.length, 1);
		}
		
		//sum elements
		for (int row = 0; row < featuresIndexes.length; row++) {
			for (int col = 0; col < features[featuresIndexes[row]].length; col++) {
				for (int classNum = 0; classNum < classNames.length; classNum++) {
					if (classLabels[col] == classNum) {
						double curr = means[classNum].get(row, 0);
						means[classNum].set(row, 0, curr + features[featuresIndexes[row]][col]);
						break;
					}
				}
			}
		}
		
		//divide sum by number of samples for each class
		for (int classNum = 0; classNum < means.length; classNum++) {
			for (int row = 0; row < means[classNum].getRowDimension(); row++) {
				means[classNum].set(row, 0, means[classNum].get(row, 0) / sampleCount[classNum]);
			}
		}
		
		return means;
	}
	
	/**
	 * Prepares samples to calculate multidimensional Fisher LD.
	 * Retrieves from the original features matrix only these feature values,
	 * which have numbers are in @param{featuresIndexes} parameter.
	 * 
	 * @param featuresIndexes 
	 * @param classNames
	 * @param classLabels
	 * @param features
	 * @return Array of prepared samples for one classification class
	 */
	public Matrix[] prepareSamples(int[] featuresIndexes, String[] classNames, int[] classLabels, double[][] features) {
		Matrix[] samples = new Matrix[classNames.length];
		//initialize matrix array
		for (int classNum = 0; classNum < classNames.length; classNum++) {
			samples[classNum] = new Matrix(featuresIndexes.length, sampleCount[classNum]);
		}
		
		int[] currClassObject = new int[classNames.length];
		
		for (int row = 0; row < featuresIndexes.length; row++) {
			Arrays.fill(currClassObject, 0);

			for (int col = 0; col < features[featuresIndexes[row]].length; col++) {
				for (int classNum = 0; classNum < classNames.length; classNum++) {
					if (classLabels[col] == classNum) {
						samples[classNum].set(row, currClassObject[classNum], features[featuresIndexes[row]][col]);
						currClassObject[classNum]++;
						break;
					}
				}
			}
		}
		
		return samples;
	}
	
	/**
	 * Populates means vector to enable subtraction with X matrix 
	 * (matrix, which contains feature values for specified class) 
	 * 
	 * @param means Array of feature means for each classification class
	 * @param samplesCount
	 * @return Array of populated means for each classification class
	 */
	private Matrix[] populateMeans(Matrix[] means, int[] samplesCount) {
		Matrix[] meansPopulated = new Matrix[means.length];
		
		for (int classNum = 0; classNum < means.length; classNum++) {
			double[][] tmp = new double[means[classNum].getRowDimension()][samplesCount[classNum]];
			
			for (int col = 0; col < samplesCount[classNum]; col++) {
				for (int row = 0; row < means[classNum].getRowDimension(); row++) {
					tmp[row][col] = means[classNum].get(row, 0); 
				}
			}
			meansPopulated[classNum] = new Matrix(tmp);
		}
		
		return meansPopulated;
	}
	
	/**
	 * Calculates covariance matrixes.
	 * @param samples
	 * @param means
	 * @return Array of covariance matrixes for each classification class
	 */
	private Matrix[] calculateCovarianceMatrixes(Matrix[] samples, Matrix[] means) {
		Matrix[] covariancesMatrixes = new Matrix[samples.length];
		for (int i = 0; i < samples.length; i++) {
			Matrix tmp = samples[i].minus(means[i]);
			Matrix mTrans = tmp.transpose();
			covariancesMatrixes[i] = tmp.times(mTrans).times(1.0/tmp.getRowDimension());
		}
		
		return covariancesMatrixes;
	}
	
	/**
	 * Calculates SFS
	 * @param dim
	 * @param classNames
	 * @param classLabels
	 * @param features
	 * @return
	 */
	private FeaturesSelectionResult calculateSFS(int dim, String[] classNames, int[] classLabels, double[][] features, boolean traceVsDet) {
		FeaturesSelectionResult result = new FeaturesSelectionResult();

		int[] winnerFeatures = new int[dim];
		Arrays.fill(winnerFeatures, -1);
		result.setFeatures(winnerFeatures);
		
		double fldTmp = 0;
		for (int i = 0; i < dim; i++) {
			result.setFisherLD(null);
			for (int f = 0; f < features.length; f++) {
				winnerFeatures[i] = f;
				
				if (!result.getFeatures().contains(f)) {
					// we need to cut winnerFeatures array, because
					// winnerFeature array contains -1 values
					int[] featuresToProcess = Arrays.copyOfRange(winnerFeatures, 0, i + 1);
					fldTmp = this.computeMultiDimFisherLD(featuresToProcess, classNames, classLabels, features, traceVsDet);

					if ((result.getFisherLD() == null) || (result.getFisherLD() != null && fldTmp > result.getFisherLD())) {
						result.setFeatures(winnerFeatures);
						result.setFisherLD(fldTmp);
					}
				}
			}
			winnerFeatures = result.getFeaturesAsDoubles();
		}

		return result;
	}
}
