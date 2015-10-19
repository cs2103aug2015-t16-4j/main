package listItLogic;


import java.io.File;

import fileModifier.FileModifier;

public class DisplayLogic {
	
	private static final String DISPLAY_DATE = "display date";
	private static final String DISPLAY_IMPT = "display impt";
	
	
	public static void determineDisplayMode(String command) {
		if (command.equals(DISPLAY_DATE)) {
			displayByDate();
		} else if (command.equals(DISPLAY_IMPT)) {
			displayByImportance();
		} else {
			defaultDisplay();
		}
	}
	private static void displayByDate() {
		FileModifier modifier = FileModifier.getInstance();
		File file = modifier.getFile();
	}
	
	private static void displayByImportance() {
		
	}
	
	private static void defaultDisplay() {
		
	}
}
