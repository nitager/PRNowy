package com.pr.ui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import Jama.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class PR_Window extends javax.swing.JFrame {


    public PR_Window() {
        initComponents();
        setSize(720, 410);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbg_F = new javax.swing.ButtonGroup();
        b_read = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        l_dataset_name_l = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        l_dataset_name = new javax.swing.JLabel();
        l_nfeatures = new javax.swing.JLabel();
        l_classCounter = new javax.swing.JLabel();
        jb_parseDSet = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        selbox_nfeat = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        f_rb_extr = new javax.swing.JRadioButton();
        f_rb_sel = new javax.swing.JRadioButton();
        b_deriveFS = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        f_combo_criterion = new javax.swing.JComboBox();
        f_combo_PCA_LDA = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        tf_PCA_Energy = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        l_NewDim = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jb_test_train = new javax.swing.JButton();
        jb_test_exe = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        tf_TrainSetSize = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jb_train = new javax.swing.JButton();
        jb_exe = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        lbl_classResult = new javax.swing.JLabel();
        tf_kValue = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        l_FLD_winner = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        l_FLD_val = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        b_read.setText("Read dataset");
        getContentPane().add(b_read);
        b_read.setBounds(20, 10, 130, 25);

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 80));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel1.setText("Dataset info");

        l_dataset_name_l.setText("Name:");

        jLabel3.setText("Classes:");

        jLabel4.setText("Features:");

        l_dataset_name.setText("...");
        l_dataset_name.setMaximumSize(new java.awt.Dimension(12, 130));

        l_nfeatures.setText("...");

        l_classCounter.setText("...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(l_dataset_name_l)
                        .addGap(18, 18, 18)
                        .addComponent(l_dataset_name, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_nfeatures))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(l_classCounter)))
                .addGap(100, 100, 100))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(l_classCounter))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_dataset_name_l)
                    .addComponent(jLabel4)
                    .addComponent(l_dataset_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_nfeatures))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(10, 50, 320, 80);

        jb_parseDSet.setText("Parse dataset");
        getContentPane().add(jb_parseDSet);
        jb_parseDSet.setBounds(190, 10, 130, 25);

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel5.setText("Feature space");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(14, 2, 118, 26);

        jLabel6.setText("FS Dimension");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(178, 9, 78, 16);

        selbox_nfeat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1" }));
        selbox_nfeat.setEnabled(false);
        jPanel3.add(selbox_nfeat);
        selbox_nfeat.setBounds(268, 6, 34, 22);
        jPanel3.add(jSeparator1);
        jSeparator1.setBounds(14, 41, 290, 10);

        f_rb_extr.setBackground(new java.awt.Color(255, 255, 204));
        rbg_F.add(f_rb_extr);
        f_rb_extr.setText("Feature extraction");
        jPanel3.add(f_rb_extr);
        f_rb_extr.setBounds(10, 110, 133, 25);

        f_rb_sel.setBackground(new java.awt.Color(255, 255, 204));
        rbg_F.add(f_rb_sel);
        f_rb_sel.setSelected(true);
        f_rb_sel.setText("Feature selection");
        jPanel3.add(f_rb_sel);
        f_rb_sel.setBounds(10, 60, 127, 25);

        b_deriveFS.setText("Derive Feature Space");
        jPanel3.add(b_deriveFS);
        b_deriveFS.setBounds(10, 180, 292, 25);

        jLabel10.setText("Criterion");
        jPanel3.add(jLabel10);
        jLabel10.setBounds(200, 50, 49, 16);

        f_combo_criterion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fisher discriminant", "Classification error" }));
        f_combo_criterion.setEnabled(false);
        jPanel3.add(f_combo_criterion);
        f_combo_criterion.setBounds(160, 70, 140, 22);

        f_combo_PCA_LDA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PCA", "LDA" }));
        f_combo_PCA_LDA.setEnabled(false);
        jPanel3.add(f_combo_PCA_LDA);
        f_combo_PCA_LDA.setBounds(190, 110, 70, 22);

        jLabel12.setText("Energy");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(20, 150, 39, 16);

        tf_PCA_Energy.setText("80");
        jPanel3.add(tf_PCA_Energy);
        tf_PCA_Energy.setBounds(70, 150, 30, 22);

        jLabel14.setText("%");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(110, 150, 20, 16);

        jLabel15.setText("New dimension:");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(160, 150, 92, 16);

        l_NewDim.setText("...");
        jPanel3.add(l_NewDim);
        l_NewDim.setBounds(270, 150, 30, 16);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(10, 140, 320, 220);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 126, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(530, 10, 160, 130);

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel8.setText("Classifier");
        jPanel4.add(jLabel8);
        jLabel8.setBounds(10, 0, 79, 26);

        jLabel9.setText("Method");
        jPanel4.add(jLabel9);
        jLabel9.setBounds(14, 44, 42, 16);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nearest neighbor (NN)", "Nearest Mean (NM)", "k-Nearest Neighbor (k-NN)", "k-Nearest Mean (k-NM)" }));
        jPanel4.add(jComboBox2);
        jComboBox2.setBounds(74, 41, 178, 22);

        jb_test_train.setText("Train");
        jPanel4.add(jb_test_train);
        jb_test_train.setBounds(30, 80, 98, 25);

        jb_test_exe.setText("Execute");
        jPanel4.add(jb_test_exe);
        jb_test_exe.setBounds(210, 90, 96, 25);

        jLabel16.setText("Training part:");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(20, 170, 80, 16);

        tf_TrainSetSize.setText("80");
        jPanel4.add(tf_TrainSetSize);
        tf_TrainSetSize.setBounds(110, 170, 20, 22);

        jLabel17.setText("%");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(140, 170, 20, 16);

        jb_train.setText("Train");
        jPanel4.add(jb_train);
        jb_train.setBounds(40, 130, 98, 25);

        jb_exe.setText("Execute");
        jPanel4.add(jb_exe);
        jb_exe.setBounds(210, 130, 96, 25);

        jLabel18.setText("Class is: ");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(110, 20, 70, 10);

        lbl_classResult.setText("_(0_o)_");
        jPanel4.add(lbl_classResult);
        lbl_classResult.setBounds(190, 10, 150, 20);

        tf_kValue.setText("3");
        jPanel4.add(tf_kValue);
        tf_kValue.setBounds(280, 170, 20, 22);

        jLabel19.setText("k - factor: ");
        jPanel4.add(jLabel19);
        jLabel19.setBounds(200, 170, 80, 16);

        getContentPane().add(jPanel4);
        jPanel4.setBounds(340, 150, 350, 210);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));
        jPanel5.setLayout(null);

        jLabel2.setText("FS Winner:");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(10, 30, 70, 16);

        l_FLD_winner.setText("xxx");
        jPanel5.add(l_FLD_winner);
        l_FLD_winner.setBounds(100, 30, 18, 16);

        jLabel13.setText("FLD value: ");
        jPanel5.add(jLabel13);
        jLabel13.setBounds(10, 60, 70, 16);

        l_FLD_val.setText("vvv");
        jPanel5.add(l_FLD_val);
        l_FLD_val.setBounds(100, 60, 48, 16);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(340, 10, 180, 130);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton b_deriveFS;
    protected javax.swing.JButton b_read;
    protected javax.swing.JComboBox f_combo_PCA_LDA;
    protected javax.swing.JComboBox f_combo_criterion;
    protected javax.swing.JRadioButton f_rb_extr;
    protected javax.swing.JRadioButton f_rb_sel;
    protected javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    protected javax.swing.JLabel jLabel18;
    protected javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    protected javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    protected javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    protected javax.swing.JButton jb_exe;
    protected javax.swing.JButton jb_parseDSet;
    protected javax.swing.JButton jb_test_exe;
    protected javax.swing.JButton jb_test_train;
    protected javax.swing.JButton jb_train;
    protected javax.swing.JLabel l_FLD_val;
    protected javax.swing.JLabel l_FLD_winner;
    protected javax.swing.JLabel l_NewDim;
    protected javax.swing.JLabel l_classCounter;
    protected javax.swing.JLabel l_dataset_name;
    private javax.swing.JLabel l_dataset_name_l;
    protected javax.swing.JLabel l_nfeatures;
    protected javax.swing.JLabel lbl_classResult;
    private javax.swing.ButtonGroup rbg_F;
    protected javax.swing.JComboBox selbox_nfeat;
    protected javax.swing.JTextField tf_PCA_Energy;
    protected javax.swing.JTextField tf_TrainSetSize;
    protected javax.swing.JTextField tf_kValue;
    // End of variables declaration//GEN-END:variables

}
