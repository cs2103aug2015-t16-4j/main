import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
