package listItUI;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public MainFrame(String title) {
		super(title);
		
		// Set layout manager
		setLayout(new BorderLayout());
		
		// Create Swing component
		TextInputPanel inputBox = new TextInputPanel();
		TextScreenPenal screenBox = new TextScreenPenal();
		
		// Add component to content pane
		Container contentPane = getContentPane();
		
		contentPane.add(inputBox, BorderLayout.SOUTH);
		contentPane.add(screenBox, BorderLayout.NORTH);
	}
}
