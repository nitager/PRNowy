/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_ui;

import stats.classifier.Classifier;
import Jama.Matrix;
import com.pr.utils.Suports;
import static com.pr.utils.Suports.getAvgValByIndexses;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import stats.classifier.FeaturesSelectionResult;
/**
 *
 * @author Piotr
 */
public class PR_Logic {
    protected Matrix a_class;
    protected Matrix b_class;
    protected Matrix a_cov;
    protected Matrix b_cov;
    protected Matrix a_Avg;
    protected Matrix b_Avg;
    protected List<Double> a_Avg_vec;
    protected List<Double> b_Avg_vec;
    protected Matrix a_partial;
    protected Matrix b_partial;
    protected List<Double> FLD = new ArrayList<Double>();
    protected List<List<Integer>> indexses_for_search;
    protected List<Integer> b_samples_indexses;
    protected List<Integer> a_samples_indexses;
    protected List<Integer> ab_rows;
    protected int a_start_index = 0;
    protected int a_last_index = 0;
    protected int b_start_index = 0;
    protected int b_last_index = 0;
    protected int n_of_sel_features = 0;
    int ClassCount = 0, FeatureCount = 0; // FutureCount uwzględnia też wiersz
    // etykiet, więc
    // trzeba go pomniejszyć o 1
    String filename = "D:\\Studia\\SMPDDuch\\PRNowy\\PR\\Test_1.txt";

    TreeMap<List<Integer>, Double> IndexesOfFSB;
    List<AbstractMap.SimpleEntry<Integer, Double>> IndexesOfFLD = new ArrayList<AbstractMap.SimpleEntry<Integer, Double>>();
    //Drzewo trzymające indeks listy indeksów dla danej wartości fishera
    protected String inputData; 			  // dataset from a text file will be placed here
    protected int classCount = 0;
    protected int featureCount = 0;
    protected double[][] features;			  // original feature matrix
    protected double[][] featuresTransformed; // and transformed feature matrix
    protected int[] classLabels;
    protected int[] sampleCount;
    protected String[] classNames;
    double[][] F, FNew; // original feature matrix and transformed feature
    public Classifier actualClassifier;

    public void set_AB_Indexses() {
        this.a_start_index = 0;
        this.a_last_index = this.sampleCount[0] - 1;
        this.b_start_index = this.F[0].length - this.sampleCount[1];
        this.b_last_index = this.F[0].length - 1;
        System.out.println(" a_start : " + a_start_index + "\n"
                + " a_last : " + a_last_index + "\n"
                + " b_start : " + b_start_index + "\n"
                + " b_last : " + b_last_index + "\n"
        );
    }

    public void setIndsforABSplit() {
        List<Integer> ab_rows_ind = new ArrayList<Integer>();// utworzenie listy
        // indeksów
        for (int i = 0; i < FeatureCount; i++) {
            ab_rows_ind.add(i);
        }
        List<Integer> b_samples = new ArrayList<Integer>();// utworzenie listy
        // indeksów gdzie
        // znajdują się sample klasy b
        for (int i = b_start_index; i <= b_last_index + 1; i++) {
            b_samples.add(i);
        }
        List<Integer> a_samples = new ArrayList<Integer>();// utworzenie listy

        // indeksów gdzie
        // znajdują się sample klasy a
        for (int i = a_start_index; i <= a_last_index; i++) {
            a_samples.add(i);
        }
        a_samples_indexses = new ArrayList<Integer>(a_samples);
        b_samples_indexses = new ArrayList<Integer>(b_samples);
        ab_rows = new ArrayList<Integer>(ab_rows_ind);
    }
     public void fillIndexsesForSearch() {

        int n_of_elements = n_of_sel_features;
        int n_of_elementsSet = FeatureCount;
        indexses_for_search = Suports.getCombinations(
                n_of_elements, n_of_elementsSet);
        // getSelectedIndex liczy do 0 więc trzeba dodać 1
        System.out.println("Kombinacje indeksow wypelnione:");
    }

 public void computeStatsForMatrixses() {
        //   IndexesOfFSB = new TreeMap<List<Integer>,Double>();
        if (a_class == null || b_class == null) {
            return;
        }
        Double best_fld_value = 0.0;
        Double worst_fld_value = 0.0;
        Integer worst_index = 0;
        Integer best_index = 0;
        Integer last_max_index = 0;
        Integer last_min_index = 0;
        Double last_best_fld_value = 0.0;
        Double last_worst_fld_value = 0.0;
        Double fisher = 0.0;
        for (int i = 0; i < indexses_for_search.size(); i++) {

            List<Integer> temp = indexses_for_search.get(i);

            fisher = computeFisherForIndexedFeatures(temp);
            FLD.add(fisher);
            System.out.println("Kombinacja indeksow +" + temp + " FSD : " + fisher);

            //to olać
            if (fisher > last_best_fld_value) {
                last_best_fld_value = fisher;
                last_max_index = i;
            }
            if (fisher < last_best_fld_value) {
                last_best_fld_value = fisher;
                last_min_index = i;
            }

        }

        //indeksy z listy kombinacji indeksów właściwości.
        //Wysznaczenie najlepszego/najgorszego współczynnika fishera
        best_fld_value = Collections.max(FLD);
        worst_fld_value = Collections.min(FLD);
        //Odnalezienie indeksów cech dla współczynników fishera
        best_index = FLD.indexOf(best_fld_value);
        worst_index = FLD.indexOf(worst_fld_value);
        //Wyswietlenie tego gówna
      

    }
  public double computeFisherForIndexedFeatures(List<Integer> indx_to_FS) {
        int[] tabIndex = Suports.toArray(indx_to_FS);//pula wybranych indeksów
        int[] tabForVec = new int[]{0};//pomaga wyciagnąć dane z macierzy średnich
        //przez jame  
        int a_cols = a_class.getColumnDimension() - 1;
        int b_cols = b_class.getColumnDimension() - 1;
        //klasa Matrix jest pochędożona i wiersze kolumny są odwrotnie niż w
        //double[][]
        a_partial = a_class.getMatrix(tabIndex, 0, a_cols);//wycinamy probki klasy a
        b_partial = b_class.getMatrix(tabIndex, 0, b_cols);//wycinamy porobki klasy b
        List<Double> a_avg_vec = getAvgValByIndexses(a_Avg_vec, indx_to_FS);//wuciecie potrzebnych średnich
        List<Double> b_avg_vec = getAvgValByIndexses(b_Avg_vec, indx_to_FS);

        //Odjecie średnich od kolum macirzy
        Matrix a_miu = Suports.matrixMinusVecByCol(a_partial, a_avg_vec);
        System.out.println("Macierz A - Avg_A");
        Suports.printDoubleMatrix(a_miu);
      
        Matrix b_miu = Suports.matrixMinusVecByCol(b_partial, b_avg_vec);
        System.out.println("Macierz B - Avg_B");
        Suports.printDoubleMatrix(b_miu);
        Matrix a_cov = computeCovarianceMatrix(a_miu.getArray()); //macierz kowariancj
        Matrix b_cov = computeCovarianceMatrix(b_miu.getArray());//macierz 
        
        System.out.println("Macierz Cov a");
        Suports.printDoubleMatrix(a_cov);
        //  zgodne z arkuszem kalkulacyjnym 
          System.out.println("Macierz Cov b");
        Suports.printDoubleMatrix(b_cov);
        
        double part_res = 0.0;
        for (int i = 0; i < b_avg_vec.size(); i++) {
            part_res += Math.pow((a_avg_vec.get(i) - b_avg_vec.get(i)), 2);

        }
        Matrix ab_to_det = a_cov.plus(b_cov);

        double fisher_top = Math.sqrt(part_res);
  //      double a_det= a_cov.det();
        //      double b_det= b_cov.det();
        double fisher_down = ab_to_det.det();
        //  zgodne z arkuszem kalkulacyjnym 
        double fisher = fisher_top / fisher_down;

        return fisher;
    }

   

    public Double fisherForSubClasses(List<Integer> ind_of_features) {

        int[] inds = Suports.toIntArray(ind_of_features);

        Matrix a_local = a_class.getMatrix(inds, 0, a_class.getColumnDimension());
        Matrix b_local = b_class.getMatrix(inds, 0, b_class.getColumnDimension());

        return 0.0;
    }

    private double[][] getPartMatrix(List<Integer> indexes) {
        double[][] result = new double[indexes.size()][F[0].length];

        int i = 0;

        for (Integer index : indexes) {
            result[i++] = F[index];
        }

        return result;
    }
     private double computeFisherLD(double[] vec) {
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

    // funkcja przycina macierz a i b wg indeksów wybranych cech i
    // fisher ośla łączka NIE UŻuwane
    private double computeFisherLD() {
        // nDims, 2-classes
        // flags - ilość cech do rozpatrzenia
        double[][] mA, mB, sA, sB;
        double sumA = 0.0, sumB = 0.0;

        double[] a_Avg = new double[a_class.getRowDimension()];
        double[] b_Avg = new double[b_class.getRowDimension()];
        // Obliczenie średniej dla każdego wiersza cech, obiektów w kolumnach
        // klasy a
        for (int row = 0; row < a_class.getRowDimension(); row++) {
            for (int col = 0; col < a_class.getColumnDimension(); col++) {

                sumA += a_class.get(row, col);

            }
            a_Avg[row] = sumA / a_class.getColumnDimension();

        }
        // Obliczenie średniej dla każdego wiersza cech, obiektów w kolumnach
        // klasy b

        for (int row = 0; row < b_class.getRowDimension(); row++) {
            for (int col = 0; col < b_class.getColumnDimension(); col++) {

                sumB += b_class.get(row, col);

            }
            b_Avg[row] = sumB / b_class.getColumnDimension();

        }

        String output = "";

        System.out.println("Liczenie FLD dla dwóch klass");

        return 0;
    }

    private double computeFisher(double[][] matrix) {
        double[][] ma0 = new double[matrix.length][1];
        double[][] ma1 = new double[matrix.length][1];
        double[][] Class_A = new double[matrix.length][sampleCount[0]];
        double[][] Class_B = new double[matrix.length][sampleCount[1]];

        double power = 0;
        double top_fisher = 0;
        double down_fisher = 0;
        double fisher = 0;

        for (int i = 0; i < matrix.length; i++) {
            double avg0 = 0;
            double avg1 = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (classLabels[i] == 0) {
                    avg0 += matrix[i][j];
                } else {
                    avg1 += matrix[i][j];
                }
                ma0[i][0] = avg0 / sampleCount[0];
                ma1[i][0] = avg1 / sampleCount[1];
            }
        }

        for (int m = 0; m < Class_A.length; m++) {
            for (int n = 0; n < Class_A[m].length; n++) {
                Class_A[m][n] = matrix[m][n] - ma0[m][0];
            }
        }

        for (int m = 0; m < Class_B.length; m++) {
            for (int n = 0; n < Class_B[m].length; n++) {
                Class_B[m][n] = matrix[m][n + sampleCount[0]] - ma1[m][0];
            }
        }

        Matrix C0 = computeCovarianceMatrix(Class_A);
        Matrix C1 = computeCovarianceMatrix(Class_B);

        down_fisher = C0.det() + C1.det();

        for (int a = 0; a <= ma0[1].length; a++) {
            double sub = ma0[a][0] - ma1[a][0];
            power += Math.pow(2, sub);
        }

        top_fisher = Math.sqrt(power);

        fisher = top_fisher / down_fisher;

        return fisher;
    }

    public String readDataSet(PR_Window window) {
        String s_tmp, s_out = "";
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(".."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Datasets - plain text files", "txt");
        jfc.setFileFilter(filter);
        if (jfc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(jfc.getSelectedFile()));
                while ((s_tmp = br.readLine()) != null) {
                    s_out += s_tmp + '$';
                }
                br.close();
                window.l_dataset_name.setText(jfc.getSelectedFile().getName());
            } catch (Exception e) {
            }
        }
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
        if (saux.length() == 0) {
            throw new Exception("The first line is empty");
        }
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
            for (int i = 0; i < NameList.size(); i++) {
                if (saux.equals(NameList.get(i))) {
                    New = false;
                    index = i; // class index
                }
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
        for (int i = 0; i < classNames.length; i++) {
            classNames[i] = NameList.get(i);
        }
        sampleCount = new int[CountList.size()];
        for (int i = 0; i < sampleCount.length; i++) {
            sampleCount[i] = CountList.get(i).intValue() + 1;
        }
        classLabels = new int[LabelList.size()];
        for (int i = 0; i < classLabels.length; i++) {
            classLabels[i] = LabelList.get(i).intValue();
        }
        classCount=classNames.length;
    }
 public void fillABMatrixses() throws Exception {// dzieli zbiór danych na
        // dane poszczegłonych klas
        try {
            set_AB_Indexses();// wyznaczenie granic indeksow obiektow klas
            setIndsforABSplit();// wyznaczenie list indeksow obiektow klas
            Matrix AB = Matrix.constructWithCopy(F);
            a_class = AB.getMatrix(0, FeatureCount - 1, a_start_index,
                    a_last_index).copy();
            b_class = AB.getMatrix(0, FeatureCount - 1, b_start_index,
                    b_last_index).copy();

            System.out.println("Macierze danych klass wypelnione:");
        } catch (Exception ex) {
           System.out.println(
                    ex.getCause() + " " + ex.getMessage() + " ");
            ex.printStackTrace();

        }

    }
 
    public void genIterationsIndexsesInt(int n_of_chosen, int n_of_feauters) {

        indexses_for_search = Suports.getCombinations(n_of_chosen, n_of_feauters);
   //     printMatrix(indexses_for_search);
    }

    public void fillAvarageForClasses() {
        a_Avg = Suports.AVG(a_class.getArray());
        b_Avg = Suports.AVG(b_class.getArray());
        a_Avg_vec = Suports.AVG_Vec(a_class.getArray());
        b_Avg_vec = Suports.AVG_Vec(b_class.getArray());
        System.out.println("Macierze srednich wypelnione:");
        System.out.println("Wektor srednich \"a\" wypelnione:");
        System.out.println(a_Avg_vec);
       
       System.out.println("Wektor srednich \"b\" wypelnione:");
       System.out.println(b_Avg_vec);
       
    }
    public void fillFeatureMatrix() throws Exception {
		// having determined array size and class labels, fills in the feature
        // matrix
        int n = 0;
        String saux, stmp = inputData;
        for (int i = 0; i < sampleCount.length; i++) {
            n += sampleCount[i];
        }
        if (n <= 0) {
            throw new Exception("no samples found");
        }
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
    }

    public FeaturesSelectionResult selectFeatures(int[] flags, int d, boolean traceVsDet) {
        // for now: check all individual features using 1D, 2-class Fisher criterion
        FeaturesSelectionResult result = new FeaturesSelectionResult();

        if (d == 1) {
            double tmp;
            for (int i = 0; i < featureCount; i++) {
                tmp = computeFisherLD(features[i]);
                if (result.getFisherLD() == null || (result.getFisherLD() != null && tmp > result.getFisherLD().doubleValue())) {
                    result.setFeature(i);
                    result.setFisherLD(tmp);
                }
            }

        } else {
            result = this.calculateSFS(d, classNames, classLabels, features, traceVsDet);
            System.out.println("\n\nWINNER: \n" + result);
        }

        return result;
    }

    public void setTransformedFeatures(FeaturesSelectionResult results) {
        featuresTransformed = new double[results.getFeatures().size()][];
        for (int i = 0; i < results.getFeatures().size(); i++) {
            featuresTransformed[i] = features[results.getFeatures().get(i)].clone();
        }
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
        System.out.println("FLD: " + nominator / denominator);

        return nominator / denominator;
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
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                mean[i] += M[i][j];
            }
        }
        for (int i = 0; i < M.length; i++) {
            mean[i] /= M[0].length;
        }
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                M[i][j] -= mean[i];
            }
        }
        return M;
    }

    public double[][] projectSamples(Matrix FOld, Matrix TransformMat) {

        return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
    }

    /**
     * Calculates means for given features in each class
     *
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
     * Prepares samples to calculate multidimensional Fisher LD. Retrieves from
     * the original features matrix only these feature values, which have
     * numbers are in @param{featuresIndexes} parameter.
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
     * Populates means vector to enable subtraction with X matrix (matrix, which
     * contains feature values for specified class)
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
     *
     * @param samples
     * @param means
     * @return Array of covariance matrixes for each classification class
     */
    private Matrix[] calculateCovarianceMatrixes(Matrix[] samples, Matrix[] means) {
        Matrix[] covariancesMatrixes = new Matrix[samples.length];
        for (int i = 0; i < samples.length; i++) {
            Matrix tmp = samples[i].minus(means[i]);
            Matrix mTrans = tmp.transpose();
            covariancesMatrixes[i] = tmp.times(mTrans).times(1.0 / tmp.getRowDimension());
        }

        return covariancesMatrixes;
    }

    /**
     * Calculates SFS
     *
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
