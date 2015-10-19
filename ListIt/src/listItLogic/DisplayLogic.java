package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import taskGenerator.Task;
import java.util.Collections;

public class DisplayLogic {
	
	private static final String DISPLAY_ALPHA = "display alphabetic";
	private static final String DISPLAY_IMPT = "display impt";
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();
	
	
	public static void determineDisplayMode(String command) {
		if (command.equals(DISPLAY_ALPHA)) {
			displayByAlpha();
		} else if (command.equals(DISPLAY_IMPT)) {
			displayByImportance();
		} else {
			defaultDisplay();
		}
	}
	private static void displayByAlpha() {
		list = modifier.getContentList();
		Collections.sort(list);
		modifier.display(list);
	}
	
	private static void displayByImportance() {
		list = modifier.getContentList();
		Collections.sort(list);
		modifier.display(list);
	}
	
	private static void defaultDisplay() {
		list = modifier.getContentList();
		Collections.sort(list);
		modifier.display(list);
	}
}
