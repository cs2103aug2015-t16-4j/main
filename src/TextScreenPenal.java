import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TextScreenPenal extends JPanel {
	
	static JTextArea displayScreen = new JTextArea(18, 33);
	
	public TextScreenPenal() {
		Dimension size = getPreferredSize();
		size.height=350;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Display"));
		
		setLayout(new GridBagLayout());
		
		displayScreen.setEditable(false);
		
		GridBagConstraints screenPenalConstraint = new GridBagConstraints();
		
		//First Column
		screenPenalConstraint.anchor = GridBagConstraints.CENTER;
		screenPenalConstraint.gridx = 0;
		screenPenalConstraint.gridy = 0;
		add(displayScreen, screenPenalConstraint);
	}

	public static void display(String content, Integer lineCount) {
		displayScreen.append(lineCount.toString() + ". " + content + "\n");
	}

	public static void sucessfulAdd(String content) {
		displayScreen.append("\"" + content + "\"" + " is added to File." + "\n\n");
	}

	public static void sucessfulClear() {
		displayScreen.append("All event cleared" + "\n\n");
	}

	public static void displayEmpty() {
		displayScreen.append("The file is Empty" + "\n\n");
	}
}
