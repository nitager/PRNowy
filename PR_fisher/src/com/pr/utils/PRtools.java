/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Piotr
 */
public class PRtools {
    
    public static List<Integer> genIntList(int n){
        List<Integer> list=new ArrayList();
        for(int i=0;i<n;i++){
        list.add(n);
        }
        
        return list;
    }
    
    public static List<List<Integer>> genCombinationListOfIndexses(int n, int k){
     List<List<Integer>> listsOfIndexes=new ArrayList<>();
        CombinationIterator<Integer> myIndexes=new CombinationIterator<Integer>(genIntList(n), k);
     while(myIndexes.hasNext()){
     listsOfIndexes.add(new ArrayList(myIndexes.next()));
     }   
        
        
    
    return listsOfIndexes;}
    
    public static List<String> genStringListOfInts(int n) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < n; i++){
        list.add(Integer.toString(i));}
        
        return list;
    }
    public static List<String> genStringListOfInts(Integer n) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < n; i++);
        list.add(Integer.toOctalString(n));
        
        return list;
    }
    
     public static String[] genStringArrayOfInts(Integer n) {
        String[] list=new String[n];
        for (int i = 0; i < n; i++){
         list[i]= Integer.toString(i);
        }
        return list;
    }
    
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
