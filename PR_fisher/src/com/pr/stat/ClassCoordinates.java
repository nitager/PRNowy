/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr.stat;

/**
 *
 * @author Piotr
 */
public class ClassCoordinates {
    int startIndex;
    int endIndex;
      public ClassCoordinates(){}
    public ClassCoordinates(int start,int end){
    this.startIndex=start;
    this.endIndex=end;
    
    }
    public int getStart(){
    return this.startIndex;
    }
    public void setStart(int start){
    this.startIndex=start;
    }
    public int getEnd(){
    return this.startIndex;
    }
    public void setEnd(int end){
    this.endIndex=end;
    } 
}
