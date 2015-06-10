/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr_ui;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Piotr
 */
public class PR_Main {
    public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PR_Window window = new PR_Window();
				PR_Logic logic = new PR_Logic();
				PR_Listeners listeneres = new PR_Listeners(window, logic);
				
				listeneres.initializeListeners();
				
				window.setVisible(true);
			}
		});
	}
}
