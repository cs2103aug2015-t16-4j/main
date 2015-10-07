package listItUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextScreenPenal extends JPanel {
	
	static JTextArea displayScreen = new JTextArea(18, 33);
	
	public TextScreenPenal() {
		JScrollPane screenPane = new JScrollPane(displayScreen);
		
		Dimension size = getPreferredSize();
		size.height=450;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Display"));
		
		setLayout(new BorderLayout());
		
		add(screenPane, BorderLayout.CENTER);
		
		displayScreen.setEditable(false);
		
		JScrollBar verticalBar = screenPane.getVerticalScrollBar();
		verticalBar.setValue(verticalBar.getMaximum());
		
		JScrollBar horizontalBar = screenPane.getHorizontalScrollBar();
		horizontalBar.setValue(horizontalBar.getMaximum());
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

	public static void deleteSucessful(String aLineOfContent) {
		displayScreen.append(aLineOfContent + " is deleted." + "\n\n");
	}

	public static void displayLine() {
		displayScreen.append("\n");
	}

	public static void displayInvalidDate() {
		displayScreen.append("Date entered is invalid, make sure you enter the correct date and format in ddmmyy or ddmmyyyy" + "\n\n");	
	}

	public static void displayNoTitle() {
		displayScreen.append("No event title is given in this add statement" + "\n\n");
	}

	public static void displayInvalidInput() {
		displayScreen.append("Invalid Input" + "\n\n");
	}

	public static void displaySuccessfulEdit() {
		displayScreen.append("Edited" + "\n\n");
	}
}
