import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

public class TextRecorder {
	
	public static void main(String[] args) {
	
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame("ListIt");
				frame.getContentPane().setForeground(Color.BLACK);
				frame.setSize(400,500);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}