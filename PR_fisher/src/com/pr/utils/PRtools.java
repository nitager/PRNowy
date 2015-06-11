/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr.utils;

/**
 *
 * @author Piotr
 */
public class PRtools {
    
   
    
    
    
    public static void printintMtx(int[][] src) {
        if (src == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        if (src[0] == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        String rowStr = "";
        String colStr = "";
        int rows = src.length;
        int cols = src[0].length;

        int r = 0, c = 0;

        for (r = 0; r < rows; r++) {

            rowStr = "";
            for (c = 0; c < cols; c++) {
                rowStr = rowStr + " |  ";

            }
            System.out.println(rowStr);
        }

    }
   
        public static void printdoubleMtx(double[][] src) {
        if (src == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        if (src[0] == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        String rowStr = "";
        String colStr = "";
        int rows = src.length;
        int cols = src[0].length;

        int r = 0, c = 0;

        for (r = 0; r < rows; r++) {

            rowStr = "";
            for (c = 0; c < cols; c++) {
                rowStr = rowStr + " |  ";

            }
            System.out.println(rowStr);
        }

    }
   
    
    public static void printIntMtx(Integer[][] src) {
        if (src == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        if (src[0] == null) {
            System.out.println("Brak danych w tablicy 2D ");
            return;
        }
        String rowStr = "";
        String colStr = "";
        int rows = src.length;
        int cols = src[0].length;

        int r = 0, c = 0;

        for (r = 0; r < rows; r++) {

            rowStr = "";
            for (c = 0; c < cols; c++) {
                rowStr = rowStr + " |  ";

            }
            System.out.println(rowStr);
        }

    }
    
}
