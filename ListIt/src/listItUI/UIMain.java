package listItUI;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class UIMain {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame("ListIt");
				frame.getContentPane().setForeground(Color.BLACK);
				frame.setSize(550, 650);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
