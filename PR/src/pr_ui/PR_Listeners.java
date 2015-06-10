/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_ui;

import Jama.Matrix;
import com.pr.utils.forSwigUI.SwigUIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import stats.classifier.Classifier;
import stats.classifier.FeaturesSelectionResult;
import stats.classifier.KNNeighbourClassifier;

/**
 *
 * @author Piotr
 */
public class PR_Listeners {
    

	
	private PR_Window window;
	private PR_Logic logic;
	
	public PR_Listeners(PR_Window window, PR_Logic logic) {
		if (window == null) {
			throw new NullPointerException("window cannot be null");
		} else if (logic == null) {
			throw new NullPointerException("login cannot be null");
		}
		
		this.window = window;
		this.logic = logic;
	}
	
	public void initializeListeners() {
		window.JB_ReadDataSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				b_readActionPerformed(evt);
			}
		});
		
		window.JB_ParseDataSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		
		window.f_rb_extr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				f_rb_extrActionPerformed(evt);
			}
		});
		
		window.f_rb_sel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				f_rb_selActionPerformed(evt);
			}
		});
		
		window.b_deriveFS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				b_deriveFSActionPerformed(evt);
			}
		});
		
		window.JB_Main_Train.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				b_TrainActionPerformed(evt);
			}
		});
		
		window.JB_Main_Exe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				executeActionPerformed(evt);
			}
		});
	}
	
	private void executeActionPerformed(ActionEvent evt) {
		double classification=logic.actualClassifier.testClassifier();
		window.lab_classResult.setText( String.valueOf(classification * 100) );
	}
	
	private void b_TrainActionPerformed(ActionEvent evt) {
		// first step: split dataset (in new feature space) into training /
		// testing parts
		if (logic.featuresTransformed == null) {
			// no reduced feature space have been derived
			return;
		}
		Classifier classifier = null;
		int k = 1;
		if (window.JCB_CalassiferType.getSelectedIndex() == 0) {
			
			classifier = new KNNeighbourClassifier(k);
			
		} else if (window.JCB_CalassiferType.getSelectedIndex() == 1) {
			
		
			
		} else if (window.JCB_CalassiferType.getSelectedIndex() == 2) {
			
			k = validateK(window.kTextField.getText());
			classifier = new KNNeighbourClassifier(k);
			
		} else {
		
		}
		
		classifier.trainClassifier(logic.featuresTransformed, window.tf_TrainSetSize.getText(), logic.classLabels);
		logic.actualClassifier = classifier;
		//Cl.generateTrainingAndTestSets(logic.featuresTransformed, window.tf_TrainSetSize.getText(),logic.classLabels);
	}
	
	private void f_rb_selActionPerformed(ActionEvent evt) {
		window.f_combo_criterion.setEnabled(true);
		window.f_combo_PCA_LDA.setEnabled(false);
	}

	private void f_rb_extrActionPerformed(ActionEvent evt) {
		window.f_combo_criterion.setEnabled(false);
		window.f_combo_PCA_LDA.setEnabled(true);
	}

	private void b_readActionPerformed(ActionEvent evt) {
		// reads in a text file; contents is placed into a variable of String type
		logic.inputData = logic.readDataSet(window);
	}

	private void jButton2ActionPerformed(ActionEvent evt) {
		// Analyze text inputted from a file: determine class number and labels
		// and number
		// of features; build feature matrix: columns - samples, rows - features
		try {
			if (logic.inputData != null) {
				logic.getDatasetParameters();
                                window.l_n_of_classes.setText(Integer.toString(logic.classCount));
				window.l_nfeatures.setText(logic.featureCount + "");
				this.setAvailableDimensions(logic.featureCount);
				logic.fillFeatureMatrix();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(window, ex.getMessage());
		}

	}

	private void b_deriveFSActionPerformed(ActionEvent evt) {
		// derive optimal feature space
		if (logic.features == null)
			return;
		if (window.f_rb_sel.isSelected()) {
			// the chosen strategy is feature selection
			int[] flags = new int[logic.featureCount];
			FeaturesSelectionResult featuresSelectionResult = logic.selectFeatures(flags, Integer.parseInt((String) window.selbox_nfeat.getSelectedItem()), false);
			logic.setTransformedFeatures(featuresSelectionResult);
			
			window.l_FLD_winner.setText(featuresSelectionResult.getFeaturesNumbersAsString());
			window.l_FLD_val.setText(featuresSelectionResult.getFisherLD() + "");
			
			
		} else if (window.f_rb_extr.isSelected()) {
//			double TotEnergy = Double.parseDouble(window.tf_PCA_Energy.getText()) / 100.0;
//			// Target dimension (if k>0) or flag for energy-based dimension
//			// (k=0)
//			int k = 0;
//			// double[][] FF = { {1,1}, {1,2}};
//			// double[][] FF = { {-2,0,2}, {-1,0,1}};
//			// F is an array of initial features, FNew is the resulting array
//			double[][] FFNorm = logic.centerAroundMean(logic.features);
//			Matrix Cov = logic.computeCovarianceMatrix(FFNorm);
//			Matrix TransformMat = logic.extractFeatures(Cov, TotEnergy, k);
//			logic.featuresTransformed = logic.projectSamples(new Matrix(FFNorm), TransformMat);
//			// FNew is a matrix with samples projected to a new feature space
//			window.l_NewDim.setText(logic.featuresTransformed.length + "");
		}
	}
	
	private void setAvailableDimensions(int featureCount) {
				
		window.selbox_nfeat.setModel(SwigUIHelper.fillComboModelWithInts(featureCount));
	}
	
	private int validateK (String kString) {
		int k =0;
		
		try {
			
			 k=Integer.parseInt(kString);
			
	
			 if (k <=0 ) {
				 throw new Exception("k nie może być mniejsze bądź równe 0");
			 }
			 if (k >=200 ) {
				 throw new Exception("k nie może być większe niż 200");
			 }
		}
		catch(NumberFormatException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, "k musi być liczbą całkowitą");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getLocalizedMessage());
		}
		
		return k;
	}
}
