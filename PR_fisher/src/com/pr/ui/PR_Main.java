package statystyczne;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
**/

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
