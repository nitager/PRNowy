/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr.utils.forSwigUI;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Piotr
 */
public class SwigUIHelper {
     public static void fillCombo(JComboBox src, int n) {
        List<String> newItems = new ArrayList<String>();
        src.removeAllItems();
        for (int i = 1; i <= n; i++) {

            newItems.add(new StringBuilder(Integer.toString(i)).toString());
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(newItems.toArray());
        src.setModel(model);
    }        
public static DefaultComboBoxModel fillComboModelWithInts(int n){
      List<String> newItems = new ArrayList<String>();
       
        for (int i = 1; i <= n; i++) {

            newItems.add(new StringBuilder(Integer.toString(i)).toString());
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(newItems.toArray());
        return model;
     }
}
