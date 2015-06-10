package com.pr.utils;

import com.pr.utils.CombinationIterator;
import Jama.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Piotr
 */
public final class Suports {

    public static List<Double> getAvgValByIndexses(List<Double> src, List<Integer> indx_to_FS) {
        List<Double> cuttedAVG = new ArrayList<Double>();
        if (indx_to_FS.size() > src.size()) {
            System.out.print("Błąd średnich");
            return cuttedAVG;

        }
        //kolejność wybierania wartości średnich jest istotna
        for (int i = 0; i < indx_to_FS.size(); i++) {
            cuttedAVG.add(src.get(indx_to_FS.get(i)));

        }
        return cuttedAVG;
    }

    private Matrix computeCovarianceMatrix(double[][] m) {
        // double[][] C = new double[M.length][M.length];

        Matrix M = new Matrix(m);
        Matrix MT = M.transpose();
        Matrix C = Matrix.constructWithCopy(M.times(MT).getArray());
        return C;
    }

    private double[][] centerAroundMean(double[][] M) {

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

    public static Map sortByValue(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list) {
            ret[i++] = e.intValue();
        }
        return ret;
    }

    //robi kopię danych średniej
    //U-AVG
    public Matrix matrixMinusAvg(Matrix src) {

        Matrix a_minus_Avg = src.minus(AVG(src.getArray()));

        return Matrix.constructWithCopy(a_minus_Avg.getArray());
    }

    public static List<Double> AVG_Vec(double[][] M) {
        Double part_mean = 0.0;
        Double elements;
        elements = new Double(M[0].length);

        List<Double> meanList = new ArrayList<Double>();
        for (int i = 0; i < M.length; i++) {
            part_mean = 0.0;
            for (int j = 0; j < elements; j++) {
                part_mean += M[i][j];
            }

            meanList.add(part_mean / elements);
        }

        return meanList;

    }

    public static int factorial(int m) {
        int ans = 1;
        for (int i = m; i >= 1; --i) {
            ans = ans * i;
        }
        return ans;
    }
        public static double[][] projectSamples(Matrix FOld, Matrix TransformMat) {

        return (FOld.transpose().times(TransformMat)).transpose()
                .getArrayCopy();
    }

    public static Matrix AVG(double[][] M) {

        double[] mean = new double[M.length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                mean[i] += M[i][j];
            }
        }
        for (int i = 0; i < M.length; i++) {
            mean[i] /= M[0].length;
        }

        Matrix avg = new Matrix(M.length, M[0].length);

        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                avg.set(i, j, mean[i]);
            }
        }

        return new Matrix(avg.getArrayCopy());

    }

    public static Matrix getPartMatrix(List<Integer> idexses_of_features, double[][] src_F) {
        double[][] result = new double[idexses_of_features.size()][src_F[0].length];

        int i = 0;

        for (Integer index : idexses_of_features) {
            result[i++] = src_F[index];
        }

        return Matrix.constructWithCopy(result);
    }

    public static List<List<Integer>> getCombinations(int num, int outOf) {
        List<List<Integer>> temp = new ArrayList<List<Integer>>();
        List<Integer> indexses = generateIndexList(outOf);
        CombinationIterator iter = CombinationIterator.combIterator(indexses.size(), num);
        while (iter.hasNext()) {

            temp.add(iter.next(indexses));
        }
        return temp;
    }

    public static Matrix matrixMinusVecByCol(Matrix a_src, List<Double> a_vec) {
        int row = 0;
        int col = 0;
        Double diference = 0.0;
        int a_row = a_src.getRowDimension();
        int a_col = a_src.getColumnDimension();
        Matrix diff = new Matrix(a_src.getArray());
        String rowStr = "\n ";
        for (row = 0; row < a_row; row++) {

            rowStr = "";
            for (col = 0; col < a_col; col++) {
                diference = a_src.get(row, col) - a_vec.get(row);
                diff.set(row, col, new Double(diference));
                rowStr = rowStr + " | " + Double.toString(diference);
            }
            System.out.println(rowStr);
        }

        return Matrix.constructWithCopy(diff.getArray());
    }

    public static int[] toArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Iterator<Integer> it = list.iterator(); it.hasNext(); ret[i++] = it
                .next())
			;
        return ret;
    }

    public static Integer getCombinationNum(Integer n, Integer k) {
        if (n < k) {
            return 0;
        }
        if (n == k) {
            return 1;
        }
        Integer delta, iMax;
        if (k < n - k) {
            delta = n - k;
            iMax = k;
        } else {
            delta = k;
            iMax = n - k;

        }
        Double ans = delta.doubleValue() + 1;
        for (Integer i = 2; i <= iMax; ++i) {
            ans = (ans * (delta + i)) / i;
        }
        return ans.intValue();

    }

    public static int[] generateIndexSet(int n) {
        int[] temp = new int[n];
        for (int i = 0; i < n; i++) {
            temp[i] = i;
        }
        return temp;
    }

    public static List<Integer> generateIndexList(int n) {
        List<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            temp.add(i);
        }
        return temp;
    }

    public static int get_nCr(int r, int n) {
        if (r > n) {
            throw new ArithmeticException("r is greater then n");
        }
        long numerator = 1;
        long denominator = 1;
        for (int i = n; i >= r + 1; i--) {
            numerator *= i;
        }
        for (int i = 2; i <= n - r; i++) {
            denominator *= i;
        }

        return (int) (numerator / denominator);
    }

    public static void printDoubleMatrix(Matrix a) {
        int rows = 0;
        int cols = 0;
        String rowStr = "";
        for (rows = 0; rows < a.getRowDimension(); rows++) {
            rowStr = "";
            for (cols = 0; cols < a.getColumnDimension(); cols++) {
                rowStr = rowStr + " | " + Double.toString(a.get(rows, cols));
            }
            System.out.println(rowStr);
        }
        System.out.println("Rows : " + rows + " Cols : " + cols);
    }

    public static void printDoubleMatrix(double[][] src) {
        int rows = 0;
        int cols = 0;
        String rowStr = "";
        for (rows = 0; rows < src.length; rows++) {
            rowStr = "";
            for (cols = 0; cols < src[0].length; cols++) {
                rowStr = rowStr + " | " + Double.toString(src[rows][cols]);
            }
            System.out.println(rowStr);
        }
        System.out.println("Rows : " + rows + " Cols : " + cols);
    }

    public static void printDoubleMatrix(Double[][] src) {
        int rows = 0;
        int cols = 0;
        String rowStr = "";
        for (rows = 0; rows < src.length; rows++) {
            rowStr = "";
            for (cols = 0; cols < src[0].length; cols++) {
                rowStr = rowStr + " | " + Double.toString(src[rows][cols]);

            }
            System.out.println(rowStr);
        }
        System.out.println("Rows : " + rows + " Cols : " + cols);
    }

    public static void testDoubleMatrixDoubleConv(double[][] src) {
        Matrix m_src = new Matrix(src);
        double[][] d_src = m_src.getArrayCopy();

        System.out.println("Macierz źródłowa");
        printDoubleMatrix(src);
        System.out.println("Macierz jako Jama Matrix");
        printDoubleMatrix(m_src);
        System.out.println("Macierz po konwersji do doubli");
        printDoubleMatrix(d_src);
        System.out.println("Macierz po transpozucji przez JamaMatrix");
        printDoubleMatrix(m_src.transpose());

    }
}
